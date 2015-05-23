//
//  ViewController.swift
//  WeatherToWakeUp
//
//  Created by Carola Nitz on 23/05/15.
//  Copyright (c) 2015 weatherToWakeUp. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    

    @IBOutlet weak var tableView: UITableView!
    let tableViewDelegate = ClocksTableViewDelegate()

    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.delegate = tableViewDelegate
        tableView.dataSource = tableViewDelegate
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

