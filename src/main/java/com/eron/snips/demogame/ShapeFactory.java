package com.eron.snips.demogame;

import java.util.Random;

public class ShapeFactory {
	private int shapes[][][] = new int[][][] { 
				{ { 1, 0, 0, 0 }, { 1, 1, 1, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, },
				{ { 1, 1, 0, 0 }, { 1, 0, 0, 0 }, { 1, 0, 0, 0 }, { 0, 0, 0, 0 }, },
				{ { 1, 1, 1, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, },
				{ { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 1, 1, 0, 0 }, { 0, 0, 0, 0 } } 
			};

	public Shape getShape(ShapeListener listener) {  // 此行中的Shape是类型，相当于其他方法的void
		System.out.println("system's getshape.");
		Shape shape = new Shape();
		shape.addShapeListener(listener);
		int type = new Random().nextInt(shapes.length);
		shape.setBody(shapes[type]);
		shape.setStatus(0);
		return shape;
	}
}





