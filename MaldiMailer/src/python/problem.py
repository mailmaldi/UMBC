from constraint import *
import time
import signal

class TimeoutException(Exception): 
    pass 

class ProblemTimer(Problem):
    def getSolutionWithTimeout(self,timeoutval):
        def timeout_handler(signum, frame):
            raise TimeoutException()
        signal.signal(signal.SIGALRM, timeout_handler) 
        signal.alarm(timeoutval)
        
        domains, constraints, vconstraints = self._getArgs()
        if not domains:
            return None
        
        try:
            return self._solver.getSolution(domains, constraints, vconstraints)
        except TimeoutException:
            print "TimeoutException"
            return None
 
  
    
