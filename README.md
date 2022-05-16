# MazeSolver
Solving Mazes given in a text file with DFS and BFS algorithms in java            


# Given File Format 
  - txt file named map -> map.txt
  
  - User has to choose the prefered method of solving whether DFS (Depth First) or BFS (Breadth First).

# Input Format 
   - you should input the size of the maze in the first line in the map on the form <row> <column>
   - you should draw your maze on the form
     1. 'S' denoting the starting point of the maze. 
     2. 'E' denoting to the End point of the maze.
     3. '.' which will be points that you are free to move through.
     4. '#' which will be blocks "you can't move through".
     example to the maze input in map.txt file :
      5 5 
      S...#
      ##.##
      #...#
      .E.##
# Output Format : 
  - the path illustrated in the map.
  - vertices of steps to be followed on that map. 
  
