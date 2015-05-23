//
//  ClocksTableViewDelegate.swift
//  WeatherToWakeUp
//
//  Created by Franzi on 23.05.15.
//  Copyright (c) 2015 weatherToWakeUp. All rights reserved.
//

import UIKit

class ClocksTableViewDelegate :  NSObject, UITableViewDelegate, UITableViewDataSource{
    let cellIdentifier = "AlarmIdentifier"
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1 // there is only one clock right now
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier(cellIdentifier, forIndexPath: indexPath) as AlarmTableCell;
        cell.clockTimeLabel.text = "6:30";
        return cell;
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        // TODO: open detail screen for clock rules
    }
}