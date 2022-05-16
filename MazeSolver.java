// import java.awt.image.ImageProducer;
import java.util.*;
import java.io.*;
import java.nio.file.Path;
import java.awt.Point;

interface IMazeSolver{
    /* Find path with BFS (Breadth First Search) algorithm */
    public int[][] solveBFS(File maze);
    /* Find path with DFS (Depth First Search) algorithm */
    public int[][] solveDFS(File maze);
}

class StackImplementation {
    public class Node { //node class with data and next
        Object data;
        Node next;
        
        public Node(Object elem, Node nxt) {
            data = elem;
            next = nxt;
        }
    }
    public Node top;
    public int size;
    public StackImplementation() {
        top = null;
        size = 0;
    }
    public Object pop() {
        //tmp to store top data to return it at the end of the method
        Object tmp = top.data;
        top = top.next;
        size--;
        return tmp;
    }
    public Object peek() {return top.data;}
    public void push(Object element) {
        //method to push element to the top of the stack
        Node n = new Node(element, top);
        top = n;
        size++;
    }
    public boolean isEmpty() {
        return top == null;
    }
    public int size() {
        return size;
    }
}

class ArrayQueue{
    public static class node{
        Object data;
        public node(Object elem){
            data = elem;
        }
    }
    public Point[] array= new Point[500];
    public int front = -1;
    public int rear = -1;
    ArrayQueue(){
        front = -1;
        rear = -1;
        array = new Point[500];
    }
    public void enqueue(Point item){
        if (rear == 499){
            System.out.println("Error");
        }
        else if (front == -1 && rear == -1){
            front = rear = 0;
        } else {
            rear++;
        }
        array[rear] = item;
    }
    public Object dequeue(){
        Point temp;
        if (front == -1 && rear == -1) {
            System.out.println("Error");
            return null;
        } else if (front == rear) {
            temp = array[rear];
            front = rear = -1;
        } else {
            temp = array[front];
            front ++;
        }
        return temp;
    }
    public boolean isEmpty(){
        return (front == -1 && rear == -1);
    }
    public int size(){
        if (front == -1 && rear == -1) {
            return 0;
        } else {
            return (rear - front + 1);
        }
    }
}

public class MazeSolver implements IMazeSolver {

    public int[][] solveBFS(File maze){
        /* Parsing Text File */
        try{
            int N, M, i, j, k = 0;
            char[][] Array = new char[1][1];
    
            // PARSE THE TEXT FILE HERE
            Scanner myReader = new Scanner(maze);
            N = myReader.nextInt();         // Number of Rows
            M = myReader.nextInt();         // Number of Cols
            String Input = new String();        // Store the content of the file in string for processing
            Array = new char[N][M];    // 2D char array to receive the input from the string -> "Input" into it
            while(myReader.hasNextLine()){
                if (myReader.hasNext()){
                    Input += myReader.next();   // Append lines in the file to the string
                }
            }
            CheckValidity(Input);
            for (i=0; i<N; i++) {
                for (j=0; j<M; j++) {
                    Array[i][j] = Input.charAt(k);
                    k ++;
                }
            }
            myReader.close();
        
            boolean[][] Visiting_State = new boolean[N][M];
            boolean[][] Complete_State = new boolean[N][M];
            ArrayQueue Queue = new ArrayQueue();
            for(i = 0; i < N; i ++ ) {
                for (j = 0; j < M; j++) {
                    if (Array[i][j] == '#' ){
                        Visiting_State[i][j] = true; // Skip Hash Characters
                        Complete_State[i][j] = true; // Skip Hash Characters
                    } else {
                        Visiting_State[i][j] = false;
                        Complete_State[i][j] = false;
                    }
                }
            }
            
            Boolean started = false;
            System.out.print("BFS: ");
            int X, Y = 0;
            for (X=0; !started; X++){
                for (Y=0; Y<M; Y++){
                    if (!started) {
                        if (Array[X][Y] == 'S'){
                            started = true;
                            break;
                        }
                    }
                }
                if (started){
                    break;
                }
            }

            Point myPoint = new Point();
            myPoint.x = X;
            myPoint.y = Y;
            Queue.enqueue(myPoint);
            
            boolean found = false;
            int END_x = 0;
            int END_y = 0;
            String output = "";
            while (!Queue.isEmpty() && !found) {
                Point curPoint = (Point)Queue.dequeue();
                ArrayList<Point> RecieveList = new ArrayList<Point>();
                RecieveList = neighbourChecker(curPoint.x , curPoint.y , N , M , Array);
                Point[] receive = RecieveList.toArray(new Point[0]);
                boolean MARK = false;
                for (Point thePoint: receive) {
                    if (!Complete_State[thePoint.x][thePoint.y] && !Visiting_State[thePoint.x][thePoint.y]){
                        MARK = true;
                        Queue.enqueue(thePoint);
                        Visiting_State[thePoint.x][thePoint.y] = true;
                        if (Array[thePoint.x][thePoint.y] == 'E') {
                            found = true;
                            END_x = thePoint.x;
                            END_y = thePoint.y;
                            break;
                        }
                    }
                }
                Complete_State[curPoint.x][curPoint.y] = true;
                if (MARK && Array[curPoint.x][curPoint.y] != 'S')
                    Array[curPoint.x][curPoint.y] = 'x';

                if (found){
                    Point temp_point = new Point();
                    temp_point.x = END_x;
                    temp_point.y = END_y;
                }
            }
            if (found)
                System.out.print(output);
            else
                System.out.println("Error: No Path Found!");
            
            if (found){
                for (int Yy=0; Yy<N; Yy++){
                    boolean gogogo = true;
                    while (gogogo){
                        gogogo = false;
                        for (int Xx=0; Xx<M; Xx++){
                            if (Array[Yy][Xx] == 'x' || Array[Yy][Xx] == '+'){
                                int neighbours = 0;
                                if ((Yy+1 < N)  && (Array[Yy+1][Xx] == 'x' || Array[Yy+1][Xx] == 'E' || Array[Yy+1][Xx] == 'S' || Array[Yy+1][Xx] == '+')) neighbours += 1;
                                if ((Yy-1 >= 0) && (Array[Yy-1][Xx] == 'x' || Array[Yy-1][Xx] == 'E' || Array[Yy-1][Xx] == 'S' || Array[Yy-1][Xx] == '+')) neighbours += 1;
                                if ((Xx+1 < M)  && (Array[Yy][Xx+1] == 'x' || Array[Yy][Xx+1] == 'E' || Array[Yy][Xx+1] == 'S' || Array[Yy][Xx+1] == '+')) neighbours += 1;
                                if ((Xx-1 >= 0) && (Array[Yy][Xx-1] == 'x' || Array[Yy][Xx-1] == 'E' || Array[Yy][Xx-1] == 'S' || Array[Yy][Xx-1] == '+')) neighbours += 1;
                                if (neighbours <= 1){
                                    Array[Yy][Xx] = '.';
                                    gogogo = true;
                                    break;
                                } else {
                                    Array[Yy][Xx] = '+';
                                }
                            }
                        }
                    }
                }

                Point[] path = new Point[512];
                int PATH_X = 0;
                System.out.println("\nPath to be followed:");
                for (int yy=0; yy<N; yy++){
                    for (int xx=0; xx<M; xx++){
                        System.out.print(Array[yy][xx]);
                        if (Array[yy][xx] == '+' || Array[yy][xx] == 'S' || Array[yy][xx] == 'E'){
                            Point myPoint3 = new Point();
                            myPoint3.x = yy;
                            myPoint3.y = xx;
                            path[PATH_X] = myPoint3;
                            PATH_X += 1;
                        }
                        
                    }
                    System.out.println("");
                }
                
                System.out.print("BFS: ");
                boolean firster = true;
                for (Point myPoint2: path){
                    if (myPoint2 == null) continue;
                    else if (myPoint2.x != 9999){
                        if (!firster)
                            System.out.printf(", {%d, %d}", myPoint2.x, myPoint2.y);
                        else
                            System.out.printf("{%d, %d}", myPoint2.x, myPoint2.y);
                            firster = false;
                    }
                }
            }


        } catch (FileNotFoundException e) {
            System.out.println("File not found! please make sure to store the MAP file in the same directory with name: map.txt");
            e.printStackTrace();
            System.exit(0);
        }

        int[][] something_until_later = new int[2][2];
        return something_until_later;
    }

    //function to check if the given element is in neighbourhood or not
    public ArrayList<Point> neighbourChecker(int i , int j , int N , int M , char[][] Array){
        ArrayList<Point> pointList = new ArrayList<Point>(); //initialize arraylist to carry points
        // We used arraylist because we don't know the number of possible neighbour elements
        int i1 = 0; // a counter to jump between points in the arraylist
        if (InBorders(i, j, N , M)){
            if (InBorders(i + 1, j, N , M) && Array[i][j] != '#') {pointList.add(new java.awt.Point(i+1,j)); i1 ++;} //check the south of our input element
            if (InBorders(i - 1 , j, N , M) && Array[i][j] != '#') {pointList.add(new java.awt.Point(i-1,j)); i1 ++;} //check  the north
            if (InBorders(i, j + 1, N , M) && Array[i][j] != '#') {pointList.add(new java.awt.Point(i,j+1)); i1 ++;} //check the east
            if (InBorders(i, j - 1, N,M) && Array[i][j] != '#') {pointList.add(new java.awt.Point(i,j-1)); i1 ++;} //check the west
        }
        return pointList;
    }
    //Method to check if the element are in the boundries of the 2d array
    public boolean InBorders(int i , int j , int N , int M){
        boolean flag = false;
        if (i >= 0 && i < N && j >= 0 && j < M) {
            flag = true;
        }
        return flag;
    }
    public static void CheckValidity(String maze){ // function to check if the input file is valid or not
        for(int counter = 0; counter < maze.length(); counter ++){
            if (maze.charAt(counter) != 'S' && maze.charAt(counter) != '.' && maze.charAt(counter) != '#' && maze.charAt(counter) != 'E' ){
                System.out.println("invalid chars in the file\nplease input the chars as specified . , # , S , E");System.exit(0);
            }
        }
    }

    public int[][] solveDFS(File maze){ //solving the maze with the depth first algorithm
        /* Parsing Text File */
        int i = 0; int j = 0; // counters
        try{
            String output = "";
            boolean found = false;
            // PARSE THE TEXT FILE HERE
            Scanner myReader = new Scanner(maze); //scanner to read the file text
            int N = myReader.nextInt(); //num of rows of the maze
            int M = myReader.nextInt(); //num of cols of the maze
            String Input = new String(); //storing the content of the file in string to process it in later stages 
            char[][] Array = new char[N][M];//2d char array to receive the input from the string -> "Input" into it
            while(myReader.hasNextLine()){ //taking the input from file as a string
                if (myReader.hasNext()){
                    Input += myReader.next(); //append the every new line in the file to the string
                }
            }
            CheckValidity(Input);
            myReader.close();
            int k = 0; // counter to use in the string 
            // the transformation between the string and the 2d char array to deal with it easily later
            for(i = 0; i < N; i ++){
                for(j = 0; j < M; j ++){
                    Array[i][j] = Input.charAt(k);
                    k ++;
                }
            }
            Point point = new Point(); //creating a point object
            StackImplementation Stack = new StackImplementation();
            for(i = 0; i < N; i ++ ){
                for(j = 0; j < M; j ++){
                    if (Array[i][j] == 'S'){ //function to push the Starting point first in the stack
                        point.x = i;
                        point.y = j;
                        Stack.push(point);
                    }
                }
            }
            boolean[][] DontVisit = new boolean[N][M]; //a boolean array to check whether the element in the maze was visited before or not
            for(i = 0; i < N; i ++ ) {
                for (j = 0; j < M; j++) {
                    if (Array[i][j] == '#' ){ DontVisit[i][j] = true;} //from the begging just exclude hash chars
                    else {DontVisit[i][j] = false;}
                }
            }
            //the algorithm implementation itself
            Point checker = new Point(); //point object to loop through the list of points in neighbourhood 
            while(Stack.size > 0){
                point = (Point) Stack.pop();
                //our counters are used here to for the purpose of receiving the output from the stack
                i = point.x; 
                j = point.y;
                if (!DontVisit[i][j]) { // if the element wasn't visited before .. just visit it and mark it as visited
                    // System.out.printf("{%d, %d}", i, j); 
                    if (output != ""){
                        output = output + ", {" + Integer.toString(i) + ", " + Integer.toString(j) + "}";
                    } else {
                        output = output + "{" + Integer.toString(i) + ", " + Integer.toString(j) + "}";
                    }
                    if (Array[i][j] != 'S' && Array[i][j] != 'E')
                        Array[i][j] = '+';
                    DontVisit[i][j] = true;
                    if (Array[i][j] == 'E'){
                        found = true;
                        break;
                    } // the termination of our algorithm
                    //the next 6 lines we just loop through the maze to find a valid node to step to it 
                    for (int countR = 0; countR < N; countR++) { 
                        for (int countC = 0; countC < M; countC++) {
                            ArrayList<Point> RecieveList = new ArrayList<Point>();
                            RecieveList = neighbourChecker(i , j , N , M , Array);
                            Point[] receive = RecieveList.toArray(new Point[0]);
                            checker.x = countR; checker.y = countC;
                            for(int counterInPA = 0; counterInPA < receive.length; counterInPA ++){
                                //check if the node is valid to push it in the stack or not
                                if (checker.equals(receive[counterInPA])){
                                    if (!DontVisit[countR][countC]){Stack.push(receive[counterInPA]);}
                                }
                            }
                        }
                    }
                }
            }
                
            if (found){
                System.out.println("\nPath to be followed:");
                for (char[] row: Array){
                    for (char character: row){
                        System.out.print(character);
                    }
                    System.out.println("");
                }
                System.out.print("DFS: ");
                System.out.print(output);

            } else
                System.out.println("Error: No Path Found!");


        } catch (FileNotFoundException e) {
            System.out.println("File not found! please make sure to store the MAP file in the same directory with name: map.txt");
            e.printStackTrace();
        }
        int[][] something_until_later = new int[2][2];
        return something_until_later;
    }

    public static void main(String[] args) {
        //reading the text file
        Scanner ReadType = new Scanner(System.in);
        java.io.File myFile = new java.io.File("map.txt");
        MazeSolver SolveThisMap = new MazeSolver(); //new Instance of the class MazeSolver
        System.out.print("Enter the method you prefer here (BFS or DFS): ");
        String type = ReadType.next();
        while(true){ //read the type from user to solve with
            if (type.equals("DFS")){ SolveThisMap.solveDFS(myFile);break;}
            else if (type.equals("BFS")){SolveThisMap.solveBFS(myFile);break;}
            else {
                System.out.print("please enter valid type and in upper case\nEnter the method again : ");
                type = ReadType.next();
            }
        }
    }
}
