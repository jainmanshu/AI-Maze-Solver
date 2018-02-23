/******************************************************************************
 *  Compilation:  javac Maze.java
 *  Execution:    java Maze.java n
 *  Dependencies: StdDraw.java
 *
 *  Generates a perfect n-by-n maze using depth-first search with a stack.
 *
 *  % java Maze 62
 *
 *  % java Maze 61
 *
 *  Note: this program generalizes nicely to finding a random tree
 *        in a graph.
 *
 ******************************************************************************/
    
import java.util.*;
import java.io.*;

public class Maze {
    private int n;                 // dimension of maze
    private boolean[][] north;     // is there a wall to north of cell i, j
    private boolean[][] east;
    private boolean[][] south;
    private boolean[][] west;
    private boolean[][] visited;
    public int[][] maze;
    public char alg_choice;
    private boolean done = false;
        
    public int getMaze(int x, int y) {
        return maze[x][y];
    }
    
    public Maze(int n) {
        this.n = n;
        StdDraw.setXscale(0, n+2);
        StdDraw.setYscale(0, n+2);
        init();
        generate();
        alg_choice = 'z';
    }

    private void init() {
        maze = new int[n+2][n+2];
        for (int x = 0; x < n+2; ++x) {
            for (int y = 0; y < n+2; ++y) {
                maze[x][y] = 1;
            }
        }
        
        // initialize border cells as already visited
        visited = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            visited[x][0] = true;

            visited[x][n+1] = true;

        }
        for (int y = 0; y < n+2; y++) {
            visited[0][y] = true;
            visited[n+1][y] = true;
  
        }


        // initialze all walls as present
        north = new boolean[n+2][n+2];
        east  = new boolean[n+2][n+2];
        south = new boolean[n+2][n+2];
        west  = new boolean[n+2][n+2];
        for (int x = 0; x < n+2; x++) {
            for (int y = 0; y < n+2; y++) {
                north[x][y] = true;
                east[x][y]  = true;
                south[x][y] = true;
                west[x][y]  = true;                
            }
        }
                    
      }


    // generate the maze
    private void generate(int x, int y) {
        visited[x][y] = true;
        maze[x][y] = 1;

        // while there is an unvisited neighbor
        while (!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y]) {

            // pick random neighbor (could use Knuth's trick instead)
            while (true) {
                double r = StdRandom.uniform(4);
                if (r == 0 && !visited[x][y+1]) {
                    north[x][y] = false;
                    south[x][y+1] = false;

                    generate(x, y + 1);
                    break;
                }
                else if (r == 1 && !visited[x+1][y]) {
                    east[x][y] = false;
                    west[x+1][y] = false;
                 
                    generate(x+1, y);
                    break;
                }
                else if (r == 2 && !visited[x][y-1]) {
                    south[x][y] = false;
                    north[x][y-1] = false;
                   
                    generate(x, y-1);
                    break;
                }
                else if (r == 3 && !visited[x-1][y]) {
                    west[x][y] = false;
                    east[x-1][y] = false;
                    
                    generate(x-1, y);
                    break;
                }
            }
        }
    }

    // generate the maze starting from lower left
    private void generate() {
        generate(1, 1);
        
        for (int x = 1; x < n+1; ++x) {
            for (int y = 1; y < n+1; ++y) {
                if (!north[x][y])
                    maze[x][y+1] = 0;
                if (!south[x][y])
                    maze[x][y-1] = 0;
                if (!east[x][y])
                    maze[x+1][y] = 0;
                if (!west[x][y])
                    maze[x-1][y] = 0;
            }
        }

/*
        // delete some random walls
        for (int i = 0; i < n; i++) {
            int x = 1 + StdRandom.uniform(n-1);
            int y = 1 + StdRandom.uniform(n-1);
            north[x][y] = south[x][y+1] = false;
        }

        // add some random walls
        for (int i = 0; i < 10; i++) {
            int x = n/2 + StdRandom.uniform(n/2);
            int y = n/2 + StdRandom.uniform(n/2);
            east[x][y] = west[x+1][y] = true;
        }
*/
     
    }



    // solve the maze using depth-first search
    private void solve_dfs(int x, int y) {
        if (x == 0 || y == 0 || x == n+1 || y == n+1) return;
        if (done || visited[x][y]) return;
        visited[x][y] = true;

        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(0);

        // reached end
        if (x == n && y == n) done = true;

        if (!north[x][y]) solve_dfs(x, y + 1);
        if (!east[x][y])  solve_dfs(x + 1, y);
        if (!south[x][y]) solve_dfs(x, y - 1);
        if (!west[x][y])  solve_dfs(x - 1, y);

        if (done) return;

        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(0);
    }
    
    public class node {
        int x;
        int y;
        int g_cost;
        int h_cost;
        int f_cost;
        
        public node(int x1, int y1) {x=x1; y=y1;}
        public node(int x1, int y1, int g1) {
            x=x1;
            y=y1;
            g_cost=g1;
            h_cost = 0;
          // h_cost = 2*(n)-x-y;
            f_cost = g_cost + h_cost;
        }
        public int getX() {return x;}
        public int getY() {return y;}
        public int g() {return g_cost;}
        public int h() {return h_cost;}
        public int f() {return f_cost;}
    }
    
    public boolean isPresent (LinkedList<node> q, node n) {
        Iterator<node> cur = q.iterator();
        node cur1 = n;
        
        while(cur.hasNext()) {
            if (cur1.getX()==n.getX() && cur1.getY()==n.getY())
                return true;
            cur1 = cur.next();
        }        
        return false;
    }
        
    private void bfs () {
        LinkedList<node> queue = new LinkedList<>();
        
        LinkedList<node> explored = new LinkedList<>();
        
        queue.add(new node(1,1));
                     
        while (true) {
            if(queue.isEmpty()){ System.out.println("\n\n*****EMPTY QUEUE >.<");break;}
            
            node temp = queue.removeFirst();
            
            int a = temp.getX();
            int b = temp.getY();
            
            visited[a][b] = true;
            explored.add(temp);
            
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.filledCircle(a + 0.5, b + 0.5, 0.25);
            StdDraw.show();
            StdDraw.pause(0);
            
            if (a==n && b==n){ System.out.println("\n\n*****SUCCESS!!!");break;}
            
            if (!north[a][b]) {
                node n = new node(a,b+1);
                if (!visited[a][b+1])// && !isPresent(queue,n))
                    queue.addLast(n);                
            }
            if (!east[a][b]) {
                node n = new node(a+1,b);
                if (!visited[a+1][b])// && !isPresent(queue,n))
                    queue.addLast(n);                
            }
            if (!south[a][b]) {
                node n = new node(a,b-1);
                if (!visited[a][b-1])// && !isPresent(queue,n))
                    queue.addLast(n);                
            }
            if (!west[a][b]) {
                node n = new node(a-1,b);
                if (!visited[a-1][b])// && !isPresent(queue,n))
                    queue.addLast(n);                
            }            
        }        
    }
    
    public void aStar () {
        PriorityQueue<node> queue = new PriorityQueue<>(new nodeComparator());
        PriorityQueue<node> closed = new PriorityQueue<>(new nodeComparator());
        queue.add(new node(1,1,0));
        
        while (true) {
            if (queue.isEmpty()) { System.out.println("\n\n*****EMPTY QUEUE >.<");break;}
            
            node temp = queue.remove();
            
            int a = temp.getX();
            int b = temp.getY();
            int g = temp.g();
            
            visited[a][b] = true;
            closed.add(temp);
            
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.filledCircle(a + 0.5, b + 0.5, 0.25);
            StdDraw.show();
            StdDraw.pause(0);
            
            if (a==n && b==n){ System.out.println("\n\n*****SUCCESS!!!");break;}
            
            if (!north[a][b]) {
                node n = new node(a,b+1,g+1);
                if (!visited[a][b+1])// && !isPresent(queue,n))
                    queue.add(n);
            }
            if (!east[a][b]) {
                node n = new node(a+1,b,g+1);
                if (!visited[a+1][b])// && !isPresent(queue,n))
                    queue.add(n);                
            }
            if (!south[a][b]) {
                node n = new node(a,b-1,g+1);
                if (!visited[a][b-1])// && !isPresent(queue,n))
                    queue.add(n);                
            }
            if (!west[a][b]) {
                node n = new node(a-1,b,g+1);
                if (!visited[a-1][b])// && !isPresent(queue,n))
                    queue.add(n);                
            }
        }
    }
    
    public class nodeComparator implements Comparator<node> {
        @Override
        public int compare (node n1, node n2) {            
            if (n1.f()>n2.f()) return 1;
            if (n1.f()<n2.f()) return -1;
            return 0;            
        }
    }
    
     
    // solve the maze starting from the start state
    public void solve() {
        for (int x = 1; x <= n; x++)
            for (int y = 1; y <= n; y++)
                visited[x][y] = false;
        done = false;
      // alg_choice = 'd';
      // solve_dfs(1, 1);
     // alg_choice = 'b';
      //bfs();
      alg_choice = 'a';
     aStar();
    }
    
    public void solve2() {
        for (int x = 1; x <= n; x++)
            for (int y = 1; y <= n; y++)
                visited[x][y] = false;
        done = false;
      //  alg_choice = 'd';
        // solve_dfs(1, 1);
       alg_choice = 'b';
       bfs();
      //  alg_choice = 'a';
      // aStar();
    }
     public void solve3() {
        for (int x = 1; x <= n; x++)
            for (int y = 1; y <= n; y++)
                visited[x][y] = false;
        done = false;
      // alg_choice = 'd';
       // solve_dfs(1, 1);
      alg_choice = 'b';
      bfs();
      //  alg_choice = 'a';
      // aStar();
    }

    // draw the maze
    public void draw() {
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(n + 0.5, n + 0.5, 0.375);
        StdDraw.filledCircle(1.5, 1.5, 0.375);

        StdDraw.setPenColor(StdDraw.BLACK);
        for (int x = 1; x <= n; x++) {
            for (int y = 1; y <= n; y++) {
                if (south[x][y]) StdDraw.line(x, y, x+1, y);
                if (north[x][y]) StdDraw.line(x, y+1, x+1, y+1);
                if (west[x][y])  StdDraw.line(x, y, x, y+1);
                if (east[x][y])  StdDraw.line(x+1, y, x+1, y+1);
            }
        }
        StdDraw.show();
        StdDraw.pause(1000);
    }
    public void clear_maze() {
        StdDraw.clear();
    }
    public int node_count() {
        int count = 0;
        for (int x = 1; x <= n; x++) {
                for (int y = 1; y <= n; y++) {
                    if (visited[x][y] == true)
                        count++;
                }
            }
        return count;
    }


    // a test client
    public static void main (String[] args) {
        int n = 30;//Integer.parseInt(args[0]);
        Maze maze = new Maze(n);
        StdDraw.enableDoubleBuffering();
        maze.draw();
        long start = System.currentTimeMillis();
        maze.solve();
        long time = System.currentTimeMillis() - start;
        System.out.println ("Time taken : " + time );
        System.out.println ("Node count : " + maze.node_count() );
        /*maze.clear_maze();
        maze.draw();
        long start1 = System.currentTimeMillis();
        maze.solve2();
        long time1 = System.currentTimeMillis() - start1;
        System.out.println ("Time taken : " + time1 );
        System.out.println ("Node count : " + maze.node_count() );
         maze.clear_maze();
        maze.draw();
        long start2 = System.currentTimeMillis();
        maze.solve3();
        long time2 = System.currentTimeMillis() - start2;
        System.out.println ("Time taken : " + time2 );
        System.out.println ("Node count : " + maze.node_count() ); */
       // maze.file_writer();
        // maze.statistics();
    }   

   
 }


