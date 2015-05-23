//
//  AlarmDetailCell.swift
//  WeatherToWakeUp
//
//  Created by Carola Nitz on 23/05/15.
//  Copyright (c) 2015 weatherToWakeUp. All rights reserved.
//

import UIKit

class AlarmDetailCell : UITableViewCell
{
    @IBOutlet var time:UILabel!
    @IBOutlet var weatherImage:UIImageView!

    required init(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
}