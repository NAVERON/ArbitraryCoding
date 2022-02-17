package com.eron.snips.algorithms.kalman;


import static java.lang.Math.pow;
import java.util.Random;
import Jama.Matrix;


public class KalManBuilder {

    public static void main(String[] args) {
        //model parameters
        double x = Math.random(), vx = Math.random(), ax = Math.random();

        //process parameters
        double dt = 1.0 / 100.0;
        double processNoiseStdev = 3;
        double measurementNoiseStdev = 5;
        double m = 0;

        //noise generators
        Random jerk = new Random();
        Random sensorNoise = new Random();

        //init filter
        KalmanFilter KF = buildKF(dt, pow(processNoiseStdev, 2) / 2, pow(measurementNoiseStdev, 2));
        KF.setX(new Matrix(new double[][]{{x}, {vx}, {ax}}));

        //simulation
        for (int i = 0; i < 1000; i++) {
            //model update
            ax += jerk.nextGaussian() * processNoiseStdev;
            vx += dt * ax;
            x += dt * vx + 0.5 * pow(dt, 2) * ax;

            //measurement realization
            m = x + sensorNoise.nextGaussian() * measurementNoiseStdev;

            //filter update
            KF.predict();
            KF.correct(new Matrix(new double[][]{{m}}));
        }

        //results
        System.out.println("True:");
        new Matrix(new double[][]{{x}, {vx}, {ax}}).print(3, 1);
        System.out.println("Last measurement:\n\n " + m + "\n");
        System.out.println("Estimate:");
        KF.getX().print(3, 1);
        System.out.println("Estimate Error Cov:");
        KF.getP().print(3, 3);
    }

    public static KalmanFilter buildKF(double dt, double processNoisePSD, double measurementNoiseVariance) {
        KalmanFilter KF = new KalmanFilter();

        //state vector
        KF.setX(new Matrix(new double[][]{{0, 0, 0}}).transpose());

        //error covariance matrix
        KF.setP(Matrix.identity(3, 3));

        //transition matrix
        KF.setF(new Matrix(new double[][]{
            {1, dt, pow(dt, 2) / 2},
            {0, 1, dt},
            {0, 0, 1}}));

        //input gain matrix
        KF.setB(new Matrix(new double[][]{{0, 0, 0}}).transpose());

        //input vector
        KF.setU(new Matrix(new double[][]{{0}}));

        //process noise covariance matrix
        KF.setQ(new Matrix(new double[][]{
            {pow(dt, 5) / 4, pow(dt, 4) / 2, pow(dt, 3) / 2},
            {pow(dt, 4) / 2, pow(dt, 3) / 1, pow(dt, 2) / 1},
            {pow(dt, 3) / 1, pow(dt, 2) / 1, pow(dt, 1) / 1}}
        ).times(processNoisePSD));

        //measurement matrix
        KF.setH(new Matrix(new double[][]{{1, 0, 0}}));

        //measurement noise covariance matrix
        KF.setR(Matrix.identity(1, 1).times(measurementNoiseVariance));

        return KF;
    }
    
}







