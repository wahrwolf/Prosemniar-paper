from morse.builder.creator import ActuatorCreator

class Afirsttry(ActuatorCreator):
    def __init__(self, name=None):
        ActuatorCreator.__init__(self, name, \
                                 "my_first_sim.actuators.aFirstTry.Afirsttry",\
                                 "aFirstTry")

