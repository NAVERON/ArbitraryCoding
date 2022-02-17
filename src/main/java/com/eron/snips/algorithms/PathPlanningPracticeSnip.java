package com.eron.snips.algorithms;

/**
 * Created by xukaifang on 16/9/9.
 */
import java.util.*;

/**
 * 应该是路径规划相关的snips代码片段
 * 联系和参考学习
 * @author eron
 *
 */
public class PathPlanningPracticeSnip {

	private static int[][] map = null;
	private static List<Node> openList = new ArrayList<Node>();// 开启列表
	private static List<Node> closeList = new ArrayList<Node>();// 关闭列表
	private static List<Node> resultList = new ArrayList<Node>();// 结果列表

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String lines = sc.nextLine();
		String[] linesStr = lines.split(" ");
		int length = linesStr.length;
		int[][] map1 = new int[length][length];
		for (int i = 0; i < length; i++) {
			map1[0][i] = Integer.parseInt(linesStr[i]);
		}
		for (int i = 1; i < length; i++) {
			String tmpLines = sc.nextLine();
			String[] tmpLinesStr = tmpLines.split(" ");
			for (int j = 0; j < length; j++) {
				map1[i][j] = Integer.parseInt(tmpLinesStr[j]);
			}
		}

		// 1代表通路、0代表障碍-----矩阵的邻接表方式记录图

		map = new int[4][6];// 地图数组 { 0, 1, 0, 0}, { 0, 0, 0, 1}, { 1, 0, 1,
		// * 0}, { 0, 0, 0, 0}};

		// map=map1;

		int Max_row = map.length;
		int MAX_col = map[0].length;
		Node startPoint = new PathPlanningPracticeSnip().new Node(0, 0, null);
		Node endPoint = new PathPlanningPracticeSnip().new Node(Max_row - 1, MAX_col - 1, null);

		seachWay(map, startPoint, endPoint, Max_row, MAX_col);
	}

	/**
	 * 搜寻最短路径
	 *
	 * @param arr
	 * @param startPoint
	 * @param endPoint
	 */
	private static boolean seachWay(int[][] arr, Node startPoint, Node endPoint, int row, int col) {
		final int CONST_HENG = 10;// 垂直方向或水平方向移动的路径评分
		final int CONST_XIE = Integer.MAX_VALUE / 2;// 斜方向移动的路径评分
		Node curNode = startPoint;
		if (startPoint.x < 0 || startPoint.y > col || endPoint.x < 0 || endPoint.y > col
				|| arr[startPoint.x][startPoint.y] == 1 || arr[endPoint.x][endPoint.y] == 1) {
			throw new IllegalArgumentException("坐标参数错误！！");
		}

		openList.add(startPoint);
		while (!openList.isEmpty() && !openList.contains(endPoint)) {
			curNode = minList(openList);
			if (curNode.x == endPoint.x && curNode.y == endPoint.y || openList.contains(endPoint)) {
				// System.out.println("找到最短路径");
				int count = 0;
				while (!(curNode.x == startPoint.x && curNode.y == startPoint.y)) {

					resultList.add(curNode);
					count++;
					if (curNode.parentNode != null) {
						curNode = curNode.parentNode;
					}
				}
				resultList.add(startPoint);
				Collections.reverse(resultList);

				for (Node node : resultList) {
					System.out.println(node.x + "," + node.y);
				}

				System.out.println(count);

				return true;
			}
			// 上
			if (curNode.y - 1 >= 0) {
				checkPath(curNode.x, curNode.y - 1, curNode, endPoint, CONST_HENG);
			}
			// 下
			if (curNode.y + 1 < col) {
				checkPath(curNode.x, curNode.y + 1, curNode, endPoint, CONST_HENG);
			}
			// 左
			if (curNode.x - 1 >= 0) {
				checkPath(curNode.x - 1, curNode.y, curNode, endPoint, CONST_HENG);
			}
			// 右
			if (curNode.x + 1 < row) {
				checkPath(curNode.x + 1, curNode.y, curNode, endPoint, CONST_HENG);
			}

			openList.remove(curNode);
			closeList.add(curNode);
		}
		System.out.println(-1);
		System.out.print("nopath");
		return false;

	}

	// 核心算法---检测节点是否通路
	private static boolean checkPath(int x, int y, Node preNode, Node endPoint, int c) {
		Node node = new PathPlanningPracticeSnip().new Node(x, y, preNode);
		// 查找地图中是否能通过
		if (map[x][y] == 1) {
			closeList.add(node);
			return false;
		}
		// 查找关闭列表中是否存在
		if (isListContains(closeList, x, y) != -1) {// 存在
			return false;
		}
		// 查找开启列表中是否存在
		int index = -1;
		if ((index = isListContains(openList, x, y)) != -1) {// 存在
			// G值是否更小，即是否更新G，F值
			if ((preNode.g + c) < openList.get(index).g) {
				countG(node, endPoint, c);
				countF(node);
				openList.set(index, node);
			}
		} else {
			// 不存在，添加到开启列表中
			node.setParentNode(preNode);
			count(node, endPoint, c);
			openList.add(node);
		}
		return true;
	}

	// 计算G,H,F值
	private static void count(Node node, Node eNode, int cost) {
		countG(node, eNode, cost);
		countH(node, eNode);
		countF(node);
	}

	// 计算G值
	private static void countG(Node node, Node eNode, int cost) {
		if (node.getParentNode() == null) {
			node.setG(cost);
		} else {
			node.setG(node.getParentNode().getG() + cost);
		}
	}

	// 计算H值
	private static void countH(Node node, Node eNode) {
		node.setF((Math.abs(node.getX() - eNode.getX()) + Math.abs(node.getY() - eNode.getY())) * 10);
	}

	// 计算F值
	private static void countF(Node node) {
		node.setF(node.getG() + node.getH());
	}

	// 集合中是否包含某个元素(-1：没有找到，否则返回所在的索引)
	private static int isListContains(List<Node> list, int x, int y) {
		for (int i = 0; i < list.size(); i++) {
			Node node = list.get(i);
			if (node.getX() == x && node.getY() == y) {
				return i;
			}
		}
		return -1;
	}

	// 找最小值
	private static Node minList(List<Node> list) {
		Iterator<Node> i = list.iterator();
		Node candidate = i.next();

		while (i.hasNext()) {
			Node next = i.next();
			if (next.compareTo(candidate) < 0)
				candidate = next;
		}
		return candidate;
	}

	// 节点类
	private class Node {
		private int x;// X坐标
		private int y;// Y坐标
		private Node parentNode;// 父类节点
		private int g;// 当前点到起点的移动耗费
		private int h;// 当前点到终点的移动耗费，即曼哈顿距离|x1-x2|+|y1-y2|(忽略障碍物)
		private int f;// f=g+h

		public Node(int x, int y, Node parentNode) {
			this.x = x;
			this.y = y;
			this.parentNode = parentNode;
		}

		public int compareTo(Node candidate) {
			return this.getF() - candidate.getF();
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public Node getParentNode() {
			return parentNode;
		}

		public void setParentNode(Node parentNode) {
			this.parentNode = parentNode;
		}

		public int getG() {
			return g;
		}

		public void setG(int g) {
			this.g = g;
		}

		public int getH() {
			return h;
		}

		public void setH(int h) {
			this.h = h;
		}

		public int getF() {
			return f;
		}

		public void setF(int f) {
			this.f = f;
		}

		public String toString() {
			return "(" + x + "," + y + "," + f + ")";
		}
	}

	// 节点比较类
	class NodeFComparator implements Comparator<Node> {
		@Override
		public int compare(Node o1, Node o2) {
			return o1.getF() - o2.getF();
		}

	}
}




