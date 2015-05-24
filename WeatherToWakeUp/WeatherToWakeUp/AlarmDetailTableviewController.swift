//
//  AlarmDetail.swift
//  WeatherToWakeUp
//
//  Created by Carola Nitz on 23/05/15.
//  Copyright (c) 2015 weatherToWakeUp. All rights reserved.
//

import UIKit

class AlarmDetailTableviewController: UITableViewController
{
    let defaultTimeDiff:CGFloat = 30.0
    let intialtime:NSDate = NSDate()

    override func viewDidLoad() {
        super.viewDidLoad()
        print("stuff")
        
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        var cell = tableView.dequeueReusableCellWithIdentifier("cell", forIndexPath: indexPath) as! AlarmDetailCell
        //sunny weather
        if (indexPath.row == 0 )
        {
            cell.weatherImage.image = UIImage(named: "rain")
            //cell.time.text = "dummytime"// initialtime -
        } else if (indexPath.row == 1){
            cell.weatherImage.image = UIImage(named: "rainsun")
            //normal time
        } else {
            cell.weatherImage.image = UIImage(named: "sun")
            //bad weather
        }
        return cell
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 3
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {

    }
}
