"""Example of Missionaries and Cannibals for use with AIMA code"""


"""
Milind Patil
milindp1@umbc.edu
"""
#! /usr/bin/python

from search import *

class MC(Problem):
    """ Classic missionaries and Cannibals problem.  A state is
     represented by a three typle (M,C,B) where M, C and B are the
     number of missionaries, cannibals and boats on the left bank,
     repsectively.  A state is legal if it is not the case that there
     are missionaries on a bank that re outnumbered by canniabls.
     Actions are that one or two people can row the boat from one bank
     to the other.  The problem is to find a sequence of actions to go
     from (3,3,1) to (0,0,0)."""

    # missionary , cannibal , where is the boat 1 means left side, 0 means right
    def __init__(self, initial=None, goal=None):
        self.initial = initial
        self.goal = goal 

    def __repr__(self):
        """returns a string representing the object"""
        return "MC(%s,%s)" % ( self.initial , self.goal)

    def goal_test (self, state):
        """returns true if state is a goal state"""
        g = self.goal
        #print "inside goal_test state=[%s %s %s] goal=[%s %s %s]" % (state[0],state[1],state[2],g[0],g[1],g[2])
        return ( state[0] == g[0] ) and ( state[1] == g[1] ) and  ( (state[2] == g[2]) or ( (self.initial[0]==0) and (self.initial[1]==0)  ) )

    def isLegalState(self,M,C,B):
        #print "inside isLegalState M=%s C=%s B=%s self.initial[0]=%s self.initial[1]=%s" % (M,C,B,self.initial[0],self.initial[1])
        if ((M >= 0) and (M<=self.initial[0])) and (C >= 0 and (C<=self.initial[1])) and ((B >= 0) and (B<=1)):
            #print "basic tests are passed"
            pass
        else:
            return False
        # validate the sizes & capacities so that successor() is as small as possible
        # check if cannibals outnumber missionaries at either end
        
        #print "inside isLegalState passed basic limits, checking edibility"

        # condition is that on either side, if there is atleast 1 M, check if there are more than 1C i.e. will M be edible
        if ( (M > 0) and (C > M)) or ( ( (self.initial[0]-M) > 0) and (self.initial[1]- C) > (self.initial[0]-M) ):
            return False
        #print "inside isLegalState passed edibility"
        return True

    def successor(self, (M,C,B)):
        """returns a list of successors to state"""
        #print "in successor function"
        successors = []
        if B == 1:
            # can do 1 step from 1->5 but have to check if the final state will be legal or not
            #print "just before checking for legal state"
            if self.isLegalState((M-1),C ,(B-1)):
                #print "move 1M right"
                successors.append(( 'move 1M right', ((M-1),C ,(B-1)) ))
            if self.isLegalState((M-2),C ,(B-1)):
                #print "move 2M right"
                successors.append(( 'move 2M right', ((M-2),C ,(B-1) )))
            if self.isLegalState(M,(C-1) ,(B-1)):
                #print "move 1C right"
                successors.append(( 'move 1C right', (M,(C-1) ,(B-1) )))
            if self.isLegalState(M,(C-2) ,(B-1)):
                #print "move 2C right"
                successors.append(( 'move 2C right', (M,(C-2) ,(B-1) )))
            if self.isLegalState((M-1),(C-1) ,(B-1)):
                #print "move 1M 1C right"
                successors.append(( 'move 1M 1C right', ((M-1),(C-1) ,(B-1) )))
        else:
            # can do 1 step from 6->10 but check if legal, else dont add to successor
            if self.isLegalState((M+1),C ,(B+1)):
                successors.append(( 'move 1M left', ((M+1),C ,(B+1) )))
            if self.isLegalState((M+2),C ,(B+1)):
                successors.append(( 'move 2M left', ((M+2),C ,(B+1) )))
            if self.isLegalState(M,(C+1) ,(B+1)):
                successors.append(( 'move 1C left', (M,(C+1) ,(B+1) )))
            if self.isLegalState(M,(C+2) ,(B+1)):
                successors.append(( 'move 2C left', (M,(C+2) ,(B+1) )))
            if self.isLegalState((M+1),(C+1) ,(B+1)):
                successors.append(( 'move 1M 1C left', ((M+1),(C+1) ,(B+1) )))
        #print "successors: initial: [%s,%s,%s] list: %s " % (M,C,B,successors)
        return successors


    """ Prints an array showing MC problems that have solutions for
    initial states with 0..M_max and 0..C_max millionaries and
    canibals, repectively.  We use breadth_first_graph_search (from
    search.py) to test for a solution.  It returns a goal node if it
    finds a solution and None if not."""
def solutions (M_max, C_max):
        # print column headers
        print 'c:', 
        for c in range(C_max + 1):
            print c,
        print
        print ' +' + ('-' * (2* (C_max + 1)))

        # print rows
        for m in range(M_max + 1):
            # print row header
            print str(m)+'|',
            # print a row
            for c in range(C_max + 1):
                #
                if breadth_first_graph_search(MC( (m,c,1),(0,0,0) )):
                    print"1",
                else:
                    print "0",
            print

def main(initial=None, goal=None):
    #print "In Main"
    searchers = [breadth_first_graph_search, breadth_first_tree_search,  depth_first_graph_search,
                 iterative_deepening_search, depth_limited_search]
    #problems =[MC((2,2,1), (2,2,1) , (0,0,0) ),MC((3,3,1), (3,3,1) , (0,0,0) ), MC((4,4,1), (4,4,1) , (0,0,0) )]

    # create a list of search problems using the ptional args
    if initial and goal:
        problems =[MC(initial=initial, goal=goal)]
    elif initial:
        problems =[MC(initial=initial , goal= (0,0,0))]
    else:        
        problems =[MC((3,3,1),(0,0,0))]
    #problems =[MC( (3,3,1) , (0,0,0) )]
    
    for p in problems:
        for s in searchers:
            print "Solution to %s found by %s" % (p, s.__name__)
            path = s(p).path()
            #print "before path.reverse()"
            path.reverse()
            print path
            print

    print "SUMMARY: successors/goal tests/states generated/solution"
    compare_searchers(problems=problems, header=['', ''], searchers=searchers)

    # for this to work, solutions has to be defined within the class MC and self to be pointed to it
    #MC( (3,3,1) , (0,0,0) ).solutions(9,9)
    print ""
    print ""
    solutions(9,9)

# if called from the command line, invoke main using the optional
# arguments for the intial and goal states if provided

if __name__ == "__main__":

    # 1st command line arg is always the command name (e.g., my.py)
    nargs = len(sys.argv)
    print "Number arguments = %s" % nargs

    # Note that we call eval on any command line arguments to convert
    # the string into a python data structure

    if nargs > 3:
        print "Usage: python mc.py [initial_state [goal_state]]"
    elif nargs == 3:
        main(initial=eval(sys.argv[1]), goal=eval(sys.argv[2]))
    elif nargs == 2:
        main(initial=eval(sys.argv[1]))
    else:
        main()
