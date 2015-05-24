//
//  ViewController.swift
//  WeatherToWakeUp
//
//  Created by Carola Nitz on 23/05/15.
//  Copyright (c) 2015 weatherToWakeUp. All rights reserved.
//

import UIKit
import AVFoundation

class ViewController: UIViewController, UITableViewDelegate {
    

    @IBOutlet weak var tableView: UITableView!
    let tableViewDelegate = ClocksTableViewDelegate()
    var timer: NSTimer?
    var coinSound = NSURL(fileURLWithPath: NSBundle.mainBundle().pathForResource("Miau", ofType: "mp3")!)
    var audioPlayer = AVAudioPlayer()
    

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let image = UIImage(named: "Image")
        let backgroundColor = UIColor(patternImage: image!)
        tableView.backgroundColor = backgroundColor

        // Do any additional setup after loading the view, typically from a nib.
        tableView.delegate = self
        tableView.dataSource = tableViewDelegate
        scheduleAlarm()
    }
    func scheduleAlarm() {
        timer = NSTimer.scheduledTimerWithTimeInterval(10.0, target: self, selector: "playSound", userInfo: nil, repeats: false)
        timer!.fire()
    }
    
    func playSound() {
//        var mySound = SystemSoundID
//        var soundURL = NSBundle.mainBundle().URLForResource("", withExtension: "mp3")        AudioServicesCreateSystemSoundID((CFURLRef)soundURL, &mySound)
//        AudioServicesPlaySystemSound(mySound)
        audioPlayer = AVAudioPlayer(contentsOfURL: coinSound, error: nil)
        audioPlayer.prepareToPlay()
        audioPlayer.play()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let vc = storyboard.instantiateViewControllerWithIdentifier("Alarm") as! UIViewController
       self.navigationController?.pushViewController(vc, animated: true)

    }
}

