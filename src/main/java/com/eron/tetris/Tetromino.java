package com.eron.tetris;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Tetromino {

    public int x, y;

    public Color color;

    public List<Piece> pieces;

    public Tetromino(Color color, Piece... pieces) {
        this.color = color;
        this.pieces = new ArrayList<>(Arrays.asList(pieces));

        for (Piece piece : this.pieces)
            piece.setParent(this);
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;

        pieces.forEach(p -> {  // 更新各组成部分 
            p.x += dx;
            p.y += dy;
        });
    }

    public void move(Direction direction) {
        move(direction.x, direction.y);
    }

    public void draw(GraphicsContext g) {
        g.setFill(color);

        pieces.forEach(p -> g.fillRect(p.x * TetrisApp.TILE_SIZE, p.y * TetrisApp.TILE_SIZE, TetrisApp.TILE_SIZE, TetrisApp.TILE_SIZE));
    }

    public void rotateBack() {
        pieces.forEach(p -> p.setDirection(p.directions.stream().map(Direction::prev).collect(Collectors.toList()).toArray(new Direction[0])));
    }

    public void rotate() {
        pieces.forEach(p -> p.setDirection(p.directions.stream().map(Direction::next).collect(Collectors.toList()).toArray(new Direction[0])));
    }

    public void detach(int x, int y) {
        pieces.removeIf(p -> p.x == x && p.y == y);
    }

    public Tetromino copy() {
        return new Tetromino(color, pieces.stream()
                .map(Piece::copy)
                .collect(Collectors.toList())
                .toArray(new Piece[0]));
    }
}
