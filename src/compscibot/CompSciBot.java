/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compscibot;

import java.util.Random;
import robocode.*;
import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html
/**
 * CompSciRobot - a robot by (your name here)
 */
public class CompSciBot extends AdvancedRobot {

    /**
     * run: CompSciRobot's default behavior
     */
    int scanField;
    int coeff;
    long lastDetection;
    double distanceToTarget, centerRadarPos;
    boolean scanning = true;
    Random r;
    BattleRules br;

    @Override
    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        setColors(Color.red, Color.blue, Color.green); // body,gun,radar

        while (true) {
            if (scanning || (getTime() - lastDetection >= 25)) {
                setTurnRadarRight(360);
                setBodyColor(Color.ORANGE);
                scanning = true;
            } else if (getTime() - lastDetection >= 10) {
                scanField = scanField + 2;
                setBodyColor(Color.YELLOW);
            } else if (getRadarHeading() == centerRadarPos) {
                setTurnRadarLeft(scanField);
                setTurnRadarRight(2 * scanField);
                setTurnRadarLeft(scanField);
                setBodyColor(Color.GREEN);
            } else {
                setTurnRadarRight(centerRadarPos - getRadarHeading());
                setBodyColor(Color.LIGHT_GRAY);
            }
            if (getEnergy() > 25) {
                setAhead(distanceToTarget);
            } else {
                setAhead(0 - distanceToTarget);
            }
            execute();
        }
    }

    /**
     * onScannedRobot: What to do when you see another robot
     *
     * @param e
     */
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // Replace the next line with any behavior you would like
        scanning = false;
        scanField = 30; //rename this var
        centerRadarPos = e.getBearing();
        lastDetection = getTime();
        distanceToTarget = e.getDistance() - 100;
//                if (e.getHeading() < 180) {
//                    coeff = 1;
//                } else {
//                    coeff = -1;
//                }
        double gunangle = ((getHeading() - getGunHeading()) + e.getBearing());
        while (gunangle > 180) {
            gunangle -= 360;
        }
        while (gunangle < -180) {
            gunangle += 360;
        }
        setTurnGunRight(gunangle);
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 15) {
            if (!isFriendly(e.getName())) {
            setFire(3);
            }
        }
        if (!(distanceToTarget <= 0)) {
            setTurnRight(e.getBearing());
        } else if (getEnergy() < 25 && (Math.abs(getX()) < br.getBattlefieldWidth() - 15) && (Math.abs(getY()) < br.getBattlefieldHeight() - 15)) {
            setTurnLeft(25);
        }

    }

    /**
     * onHitByBullet: What to do when you're hit by a bullet
     *
     * @param e
     */
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
        //back(10);
    }

    /**
     * onHitWall: What to do when you hit a wall
     *
     * @param e
     */
    @Override
    public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
        //back(20);
        //turnLeft(180);
    }

    public boolean isFriendly(String n) { //check package name and not robot name, also make sure this stays the same between bots
        return n.startsWith("me.rishshadra");
    }

}
