from morse.builder import *
from my_first_sim.builder.actuators import Afirsttry


class Bewegungsprimitiv(GroundRobot):

    """
    A template robot model for bewegungsPrimitiv, with a motion controller and a pose sensor.
    """
    def __init__(self, name = None, debug = True):

        # bewegungsPrimitiv.blend is located in the data/robots directory
        Robot.__init__(self, 'my_first_sim/robots/bewegungsPrimitiv.blend', name)
        self.properties(classpath = "my_first_sim.robots.bewegungsPrimitiv.Bewegungsprimitiv")

        ###################################
        # Actuators
        ###################################


	#create a new aFirstTry actuator
        aFirstTry = Afirsttry()
        self.append(aFirstTry)

        # (v,w) motion controller
        # Check here the other available actuators:
        # http://www.openrobots.org/morse/doc/stable/components_library.html#actuators
        self.motion = MotionVW()
        self.append(self.motion)

        # Optionally allow to move the robot with the keyboard
        if debug:
            keyboard = Keyboard()
            keyboard.properties(ControlType = 'Position')
            self.append(keyboard)

        ###################################
        # Sensors
        ###################################

        self.pose = Pose()
        self.append(self.pose)

