#! /usr/bin/python

"""
  Stubt for code to solve the jealous husbands problem for use with
  AIMA code.  YOUR NAME, YOUR_EMAIL@UMBC.EDU
"""

from search import *

"""
  A state is represented as ...  The initial state is ... and goal
  state is ... .
"""

class JH(Problem):

    def __init__(self, initial=...):
        self.initial = initial
        self.goal = None
        pass
        
    def __repr__(self):
        """returns a string representing the object"""
        pass

    def goal_test (self, state):
        """returns true if state is a goal state"""
        pass    

    def successor(self, s):
        """returns a list of successors to state.  Just look up the
           answer in the precomputed dictionary"""
        pass


def main():
    # Don't call tree-search because it will take too long

    searchers = [breadth_first_graph_search, depth_first_graph_search,\
                 iterative_deepening_search, depth_limited_search]

    for s in searchers:
        p = JH()
        ip = InstrumentedProblem(p)
        solution = s(ip)
        path = solution.path()
        path.reverse()
        print ip
        print "Solution by %s of length %s." % (s.__name__, len(path))
        print path
        print

# If called from the command line, invoke main()
if __name__ == "__main__":
    main()

