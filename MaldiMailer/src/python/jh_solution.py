#! /usr/bin/python

"""
Milind Patil
milindp1@umbc.edu
"""

from search import *

"""
  A state is represented as (w1,h1,w2,h2,w3,h3,b)  The initial state is (1,1,1,1,1,1,1) and goal
  state is (0,0,0,0,0,0,0)

  dont like this static representation
  TODO: make it generic so that less actions be taken, maybe use a 2 d array, or a dynamically initiallized array so that solution for 0,1,2,3 can be found

  possible actions:

  To right: total 15 states
  w1 w1h1 w1w2 w1w3
  h1 h1h2 h1h3
  w2 w2h2 w2w3
  h2 h2h3
  w3 w3h3
  h3

  To left: same actions
  so total 15*2 states
"""

class JH(Problem):

    def __init__(self, initial=(1,1,1,1,1,1,1) , goal = (0,0,0,0,0,0,0) ):
        self.initial = initial
        self.goal = goal
        
    def __repr__(self):
        """returns a string representing the object"""
        return "JH(%s,%s)" % ( self.initial , self.goal)

    def goal_test (self, state):
        """returns true if state is a goal state"""
        #print "Goal test state=%s" % ()
        g=self.goal
        #this would be easy to code using range loop betn arbitrary sized state and goal
        return ( state[0] == g[0] ) and ( state[1] == g[1] ) and ( state[2] == g[2] ) and ( state[3] == g[3] ) and ( state[4] == g[4] ) and ( state[5] == g[5] ) and ( state[6] == g[6] ) 

    def successor(self, s):
        """returns a list of successors to state.  Just look up the
           answer in the precomputed dictionary"""
        successors = []
        B = s[6]
        if B == 1:
            # have to expand for all 15 moves, so define the new state with a new move

            #w1 w1h1 w1w2 w1w3
            cs = list(s)
            cs[0] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move w1 right', tuple(cs) ))

            cs = list(s)
            cs[0] -= 1
            cs[1] -= 1
            cs[6] -= 1 
            if self.isLegalState(cs):
                successors.append(( 'move w1h1 right', tuple(cs) ))

            cs = list(s)
            cs[0] -= 1
            cs[2] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move w1w2 right', tuple(cs) ))

            cs = list(s)
            cs[0] -= 1
            cs[4] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move w1w3 right', tuple(cs) ))

            #   h1 h1h2 h1h3
            cs = list(s)
            cs[1] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move h1 right', tuple(cs) ))

            cs = list(s)
            cs[1] -= 1
            cs[3] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move h1h2 right', tuple(cs) ))

            cs = list(s)
            cs[1] -= 1
            cs[5] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move h1h3 right', tuple(cs) ))

            #   w2 w2h2 w2w3
            cs = list(s)
            cs[2] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move w2 right', tuple(cs) ))

            cs = list(s)
            cs[2] -= 1
            cs[3] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move w2h2 right', tuple(cs) ))

            cs = list(s)
            cs[2] -= 1
            cs[4] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move w2w3 right', tuple(cs) ))
            
            #   h2 h2h3
            cs = list(s)
            cs[3] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move h2 right', tuple(cs) ))

            cs = list(s)
            cs[3] -= 1
            cs[5] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move h2h3 right', tuple(cs) ))
                
            #   w3 w3h3
            cs = list(s)
            cs[4] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move w3 right', tuple(cs) ))

            cs = list(s)
            cs[4] -= 1
            cs[5] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move w3h3 right', tuple(cs) ))
            
            #   h3
            cs = list(s)
            cs[5] -= 1
            cs[6] -= 1
            if self.isLegalState(cs):
                successors.append(( 'move h3 right', tuple(cs) ))

            
        else:
            
            #w1 w1h1 w1w2 w1w3
            cs = list(s)
            cs[0] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move w1 left', tuple(cs) ))

            cs = list(s)
            cs[0] += 1
            cs[1] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move w1h1 left', tuple(cs) ))

            cs = list(s)
            cs[0] += 1
            cs[2] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move w1w2 left', tuple(cs) ))

            cs = list(s)
            cs[0] += 1
            cs[4] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move w1w3 left', tuple(cs) ))

            #   h1 h1h2 h1h3
            cs = list(s)
            cs[1] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move h1 left', tuple(cs) ))

            cs = list(s)
            cs[1] += 1
            cs[3] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move h1h2 left', tuple(cs) ))

            cs = list(s)
            cs[1] += 1
            cs[5] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move h1h3 left', tuple(cs) ))

            #   w2 w2h2 w2w3
            cs = list(s)
            cs[2] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move w2 left', tuple(cs) ))

            cs = list(s)
            cs[2] += 1
            cs[3] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move w2h2 left', tuple(cs) ))

            cs = list(s)
            cs[2] += 1
            cs[4] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move w2w3 left', tuple(cs) ))
            
            #   h2 h2h3
            cs = list(s)
            cs[3] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move h2 left', tuple(cs) ))

            cs = list(s)
            cs[3] += 1
            cs[5] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move h2h3 left', tuple(cs) ))
                
            #   w3 w3h3
            cs = list(s)
            cs[4] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move w3 left', tuple(cs) ))

            cs = list(s)
            cs[4] += 1
            cs[5] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move w3h3 left', tuple(cs) ))
            
            #   h3
            cs = list(s)
            cs[5] += 1
            cs[6] += 1
            if self.isLegalState(cs):
                successors.append(( 'move h3 left', tuple(cs) ))

        print ("%s successors = %s") % (len(successors),successors)
        return successors

    def isLegalState(self,state):
        #print "isLegalState %s" % state
        for i in range(len(state)):
            if (state[i] <0) or (state[i] > 1 ):
                return False;
        """
        # ok this wont work, since a wife can be left alone or with other wives
        if (i%2 ==0) and (i not= len(state)): # basically checking all wives and ignoring the final boat since it is also even
            if (state[i] == 1) and ((state[i+1] not = 1) or ): # if wife is present in the next state then check presence of husband too , Note: what about previous bank? this is just checking the right bank. Is the constraint automatically satisfied on the left bank?
                return False;
        """
        s = state
        if (s[0] == 1):
            #if wife is present on this side, then check if she is alone or with only other wives or has husband
            # rather we need to return a False condition, which is if there are men present and she has no husband
            if (s[1] == 0 ) and ((s[3]==1) or (s[5]==1)):
                return False
        else:
            # wife is on other side, check for false condition again
            if (s[1] == 1) and ((s[3]==0) or (s[5]==0)):
                return False

        if (s[2] == 1):
            #if wife is present on this side, then check if she is alone or with only other wives or has husband
            # rather we need to return a False condition, which is if there are men present and she has no husband
            if (s[3] == 0 ) and ((s[1]==1) or (s[5]==1)):
                return False
        else:
            # wife is on other side, check for false condition again
            if (s[3] == 1) and ((s[1]==0) or (s[5]==0)):
                return False

        if (s[4] == 1):
            #if wife is present on this side, then check if she is alone or with only other wives or has husband
            # rather we need to return a False condition, which is if there are men present and she has no husband
            if (s[5] == 0 ) and ((s[1]==1) or (s[3]==1)):
                return False
        else:
            # wife is on other side, check for false condition again
            if (s[5] == 1) and ((s[1]==0) or (s[3]==0)):
                return False
        
        # now we have checked that values are in bound & conditions are not violated, can return True now
        return True;


def main():
    # Don't call tree-search because it will take too long

    #searchers = [breadth_first_graph_search, depth_first_graph_search,iterative_deepening_search, depth_limited_search]
    searchers = [breadth_first_graph_search]

    for s in searchers:
        p = JH( (1,1,1,1,1,1,1), (0,0,0,0,0,0,0))
        ip = InstrumentedProblem(p)
        solution = s(ip)
        if solution:
            path = solution.path()
            path.reverse()
            print '\n%s finds a solution of length %s that' % (s.__name__, len(path))
            print '  Expanded %s states' % ip.succs
            print '  Called the goal test %s times' % ip.goal_tests
            print '  Added %s states to the graph' % ip.goal_tests
            print '  The actions on the solution path are: START',
            for node in path[1:]:
                print '=>', node.action,
            print '=> DONE \n '
        else:
            print '%s did not find a solution' % s.__name__

        """
        print ip
        print "Solution by %s of length %s." % (s.__name__, len(path))
        print path
        print
        """

# If called from the command line, invoke main()
if __name__ == "__main__":
    main()

