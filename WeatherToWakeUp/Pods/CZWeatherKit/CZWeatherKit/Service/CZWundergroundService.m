//
//  CZWundergroundService.m
//  Copyright (c) 2014, Comyar Zaheri, http://comyar.io
//  All rights reserved.
//
//  Redistribution and use in source and binary forms, with or without
//  modification, are permitted provided that the following conditions are met:
//
//  * Redistributions of source code must retain the above copyright notice, this
//    list of conditions and the following disclaimer.
//
//  * Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
//
//  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
//  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
//  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
//  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
//  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
//  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
//  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
//  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
//  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
//  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


#pragma mark - Imports

#import "NSString+CZWeatherKit_Substring.h"
#import "CZWeatherService_Internal.h"
#import "CZMacros.h"
#import "CZWundergroundService.h"
#import "CZWeatherCondition.h"
#import "CZWeatherRequest.h"


#pragma mark - Constants

// Base URL
static NSString * const base        = @"http://api.wunderground.com/api";

// Name of the service
static NSString * const serviceName = @"Weather Underground";


#pragma mark - CZWundergroundService Implementation

@implementation CZWundergroundService
@synthesize key = _key, serviceName = _serviceName;

#pragma mark Creating a Weather Service

- (instancetype)init
{
    if (self = [super init]) {
        
    }
    return self;
}

- (instancetype)initWithKey:(NSString *)key
{
    if (self = [super init]) {
        _key            = key;
        _serviceName    = serviceName;
    }
    return self;
}

+ (instancetype)serviceWithKey:(NSString *)key
{
    return [[CZWundergroundService alloc]initWithKey:key];
}

#pragma mark Using a Weather Service

- (NSURL *)urlForRequest:(CZWeatherRequest *)request
{
    if ([self.key length] == 0) {
        return nil;
    }
    
    NSString *url = [NSString stringWithFormat:@"%@/%@/", base, self.key];
    
    if (request.requestType == CZCurrentConditionsRequestType) {
        url = [url stringByAppendingString:@"conditions/"];
    }else if (request.requestType == CZForecastRequestType && request.detailLevel == CZWeatherRequestLightDetail) {
        url = [url stringByAppendingString:@"forecast/"];
    } else if (request.requestType == CZForecastRequestType && request.detailLevel == CZWeatherRequestFullDetail) {
        url = [url stringByAppendingString:@"forecast10day/"];
    }
    
    if ([request.language length] > 0) {
        url = [url stringByAppendingString:[NSString stringWithFormat:@"lang:%@/", request.language]];
    }
    
    url = [url stringByAppendingString:@"q/"];
    
    if (request.location.locationType == CZWeatherLocationCoordinateType) {
        CGPoint coordinate = [request.location.locationData[CZWeatherLocationCoordinateName]CGPointValue];
        url = [url stringByAppendingFormat:@"%.4f,%.4f", coordinate.x, coordinate.y];
        
    } else if (request.location.locationType == CZWeatherLocationZipcodeType) {
        url = [url stringByAppendingString:request.location.locationData[CZWeatherLocationZipcodeName]];
        
    } else if (request.location.locationType == CZWeatherLocationAutoIPType) {
        url = [url stringByAppendingString:@"autoip"];
        
    } else if (request.location.locationType == CZWeatherLocationCityStateType) {
        NSString *city = request.location.locationData[CZWeatherLocationCityName];
        city = [city stringByReplacingOccurrencesOfString:@" " withString:@"_"];
        NSString *state = request.location.locationData[CZWeatherLocationStateName];
        url = [url stringByAppendingFormat:@"%@/%@", state, city];
        
    } else if (request.location.locationType == CZWeatherLocationCityCountryType) {
        NSString *city = request.location.locationData[CZWeatherLocationCityName];
        city = [city stringByReplacingOccurrencesOfString:@" " withString:@"_"];
        NSString *country = request.location.locationData[CZWeatherLocationCountryName];
        country = [country stringByReplacingOccurrencesOfString:@" " withString:@"_"];
        url = [url stringByAppendingFormat:@"%@/%@", country, city];
        
    } else {
        return nil;
    }
    
    url = [url stringByAppendingString:@".json"];
    
    return [NSURL URLWithString:url];
}

- (id)weatherDataForResponseData:(NSData *)data request:(CZWeatherRequest *)request
{
    NSDictionary *JSON = [NSJSONSerialization JSONObjectWithData:data
                                                         options:NSJSONReadingAllowFragments
                                                           error:nil];
    if (!JSON) {
        return nil;
    }
    
    if (request.requestType == CZCurrentConditionsRequestType) {
        return [self parseCurrentConditionsFromJSON:JSON];
    } else if (request.requestType == CZForecastRequestType) {
        return [self parseForecastFromJSON:JSON];
    }
    
    return nil;
}

#pragma mark Helpers

- (CZWeatherCondition *)parseCurrentConditionsFromJSON:(NSDictionary *)JSON
{
    CZWeatherCondition *condition = [CZWeatherCondition new];
    
    NSDictionary *currentObservation = JSON[@"current_observation"];
    
    NSTimeInterval epoch = [currentObservation[@"observation_epoch"]doubleValue];
    condition.date = [NSDate dateWithTimeIntervalSince1970:epoch];
    condition.summary = currentObservation[@"weather"];
    condition.climaconCharacter = [self climaconCharacterForDescription:condition.summary];
    condition.temperature = (CZTemperature){[currentObservation[@"temp_f"]floatValue], [currentObservation[@"temp_c"]floatValue]};
    condition.windDegrees = [currentObservation[@"wind_degrees"]floatValue];
    condition.windSpeed = (CZWindSpeed){[currentObservation[@"wind_mph"]floatValue],[currentObservation[@"wind_kph"]floatValue]};
    condition.humidity = [[currentObservation[@"relative_humidity"]stringByReplacingOccurrencesOfString:@"%" withString:@""]floatValue];
    
    return condition;
}

- (NSArray *)parseForecastFromJSON:(NSDictionary *)JSON
{
    NSMutableArray *forecasts = [NSMutableArray new];
    
    NSArray *forecastDay = JSON[@"forecast"][@"simpleforecast"][@"forecastday"];
    
    for (NSDictionary *day in forecastDay) {
        CZWeatherCondition *condition = [CZWeatherCondition new];
        
        NSTimeInterval epoch = [day[@"date"][@"epoch"]doubleValue];
        condition.date = [NSDate dateWithTimeIntervalSince1970:epoch];
        condition.summary = day[@"conditions"];
        condition.highTemperature = (CZTemperature){[day[@"high"][@"fahrenheit"]floatValue], [day[@"high"][@"celsius"]floatValue]};
        condition.lowTemperature = (CZTemperature){[day[@"low"][@"fahrenheit"]floatValue], [day[@"low"][@"celsius"]floatValue]};
        condition.climaconCharacter = [self climaconCharacterForDescription:condition.summary];
        condition.humidity = [day[@"avehumidity"]floatValue];
        condition.windSpeed = (CZWindSpeed){[day[@"avewind"][@"mph"]floatValue], [day[@"avewind"][@"kph"]floatValue]};
        condition.windDegrees = [day[@"avewind"][@"degrees"]floatValue];
        [forecasts addObject:condition];
    }
    
    return [forecasts copy];
}

- (Climacon)climaconCharacterForDescription:(NSString *)description
{
    Climacon icon = ClimaconSun;
    NSString *lowercaseDescription = description.lowercaseString;
    
    if([lowercaseDescription cz_contains:@"clear"]) {
        icon = ClimaconSun;
    } else if([lowercaseDescription cz_contains:@"cloud"]) {
        icon = ClimaconCloud;
    } else if([lowercaseDescription cz_contains:@"drizzle"]) {
        icon = ClimaconDrizzle;
    } else if([lowercaseDescription cz_contains:@"showers"]) {
        icon = ClimaconShowers;
    } else if([lowercaseDescription cz_contains:@"rain"] ||
              [lowercaseDescription cz_contains:@"thunderstorm"]) {
        icon = ClimaconRain;
    } else if ([lowercaseDescription cz_contains:@"hail"]) {
        icon = ClimaconHail;
    } else if([lowercaseDescription cz_contains:@"snow"] ||
              [lowercaseDescription cz_contains:@"ice"]) {
        icon = ClimaconSnow;
    } else if([lowercaseDescription cz_contains:@"fog"]) {
        icon = ClimaconFog;
    } else if ([lowercaseDescription cz_contains:@"overcast"] ||
               [lowercaseDescription cz_contains:@"smoke"]    ||
               [lowercaseDescription cz_contains:@"dust"]     ||
               [lowercaseDescription cz_contains:@"ash"]      ||
               [lowercaseDescription cz_contains:@"mist"]     ||
               [lowercaseDescription cz_contains:@"haze"]     ||
               [lowercaseDescription cz_contains:@"spray"]    ||
               [lowercaseDescription cz_contains:@"squall"]) {
        icon = ClimaconHaze;
    }
    return icon;
}

@end
