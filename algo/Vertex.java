import java.util.*;

public class Vertex {

    public int Value;

    public boolean Hit;

    public Vertex(int val) {
        Value = val;
        Hit = false;
    }
}

class SimpleGraph {

    Vertex[] vertex;

    int[][] m_adjacency;

    int max_vertex;

    public SimpleGraph(int size) {
        max_vertex = size;
        m_adjacency = new int[size][size];
        vertex = new Vertex[size];
    }

    public void AddVertex(int value) {
        // ваш код добавления новой вершины
        // с значением value
        // в незанятую позицию vertex
        int index = findAvailableIndex();
        if (index >= 0) {
            vertex[index] = new Vertex(value);
        }
    }

    // здесь и далее, параметры v -- индекс вершины
    // в списке  vertex
    public void RemoveVertex(int v) {
        // ваш код удаления вершины со всеми её рёбрами
        if (checkIndex(v)) {
            vertex[v] = null;
            for (int i = 0; i < max_vertex; i++) {
                RemoveEdge(v, i);
            }
        }
    }

    public boolean IsEdge(int v1, int v2) {
        // true если есть ребро между вершинами v1 и v2
        if (checkIndex(v1) && checkIndex(v2)) {
            return m_adjacency[v1][v2] == 1 && m_adjacency[v2][v1] == 1;
        }
        return false;
    }

    public void AddEdge(int v1, int v2) {
        // добавление ребра между вершинами v1 и v2
        if (checkIndex(v1) && checkIndex(v2)) {
            m_adjacency[v1][v2] = 1;
            m_adjacency[v2][v1] = 1;
        }
    }

    public void RemoveEdge(int v1, int v2) {
        // удаление ребра между вершинами v1 и v2
        if (checkIndex(v1) && checkIndex(v2)) {
            m_adjacency[v1][v2] = 0;
            m_adjacency[v2][v1] = 0;
        }
    }

    public ArrayList<Vertex> DepthFirstSearch(int VFrom, int VTo) {
        // Узлы задаются позициями в списке vertex.
        // Возвращается список узлов -- путь из VFrom в VTo.
        // Список пустой, если пути нету.
        ArrayList<Vertex> vertices = new ArrayList<>();
        if (checkIndex(VFrom) && checkIndex(VTo)) {
            for (int i = 0; i < max_vertex; i++) vertex[i].Hit = false;
            ArrayDeque<Integer> verticesHit = new ArrayDeque<>();
            vertex[VFrom].Hit = true;
            verticesHit.push(VFrom);
            int currentVertex = VFrom;
            DFS:
            while (!verticesHit.isEmpty()) {
                if (m_adjacency[currentVertex][VTo] == 1) {
                    verticesHit.push(VTo);
                    break;
                }
                for (int i = 0; i < max_vertex; i++) {
                    if (m_adjacency[currentVertex][i] == 1 && !vertex[i].Hit) {
                        vertex[i].Hit = true;
                        verticesHit.push(i);
                        currentVertex = i;
                        continue DFS;
                    }
                }
                verticesHit.pop();
                if (verticesHit.isEmpty()) return vertices;
                currentVertex = verticesHit.peek();
            }
            while (!verticesHit.isEmpty()) {
                vertices.add(vertex[verticesHit.pollLast()]);
            }
        }
        return vertices;
    }

    public ArrayList<Vertex> BreadthFirstSearch(int VFrom, int VTo) {
        // Узлы задаются позициями в списке vertex.
        // Возвращается список узлов -- путь из VFrom в VTo.
        // Список пустой, если пути нету.
        ArrayList<Vertex> vertices = new ArrayList<>();
        if (!checkIndex(VFrom) || !checkIndex(VTo)) return vertices;
        if (VFrom == VTo) {
            vertices.add(vertex[VFrom]);
            return vertices;
        }
        for (int i = 0; i < max_vertex; i++) vertex[i].Hit = false;
        int[] prevVertices = new int[max_vertex];
        Arrays.fill(prevVertices, -1);
        ArrayDeque<Integer> verticesHit = new ArrayDeque<>();
        vertex[VFrom].Hit = true;
        verticesHit.offer(VFrom);
        BFS:
        while (!verticesHit.isEmpty()) {
            int currentVertex = verticesHit.poll();
            for (int i = 0; i < max_vertex; i++) {
                if (m_adjacency[currentVertex][i] == 1 && !vertex[i].Hit) {
                    verticesHit.offer(i);
                    vertex[i].Hit = true;
                    prevVertices[i] = currentVertex;
                    if (i == VTo) break BFS;
                }
            }
        }
        return tracePath(prevVertices, vertices, VTo);
    }

    public ArrayList<Vertex> WeakVertices() {
        // возвращает список узлов вне треугольников
        ArrayList<Vertex> weakVertices = new ArrayList<>();
        for (int i = 0; i < max_vertex; i++) {
            if (!findTriangle(i)) weakVertices.add(vertex[i]);
        }
        return weakVertices;
    }

    private int findAvailableIndex() {
        for (int i = 0; i < max_vertex; i++) {
            if (vertex[i] == null) return i;
        }
        return -1;
    }

    private boolean checkIndex(int index) {
        return index >= 0 && index < max_vertex;
    }

    private boolean findTriangle(int vertexIndex) {
        for (int i = 0; i < max_vertex; i++) {
            if (m_adjacency[vertexIndex][i] == 1 && vertexIndex != i) {
                for (int j = 0; j < max_vertex; j++) {
                    if (j == vertexIndex) continue;
                    if (m_adjacency[i][j] == 1 && m_adjacency[vertexIndex][j] == 1) return true;
                }
            }
        }
        return false;
    }

    private ArrayList<Vertex> tracePath(int[] prevVertices, ArrayList<Vertex> vertices, int vertexTo) {
        if (prevVertices[vertexTo] == -1) return vertices;
        int currentVertex = vertexTo;
        while (currentVertex != -1) {
            vertices.add(vertex[currentVertex]);
            currentVertex = prevVertices[currentVertex];
        }
        Collections.reverse(vertices);
        return vertices;
    }
}
