package frc.robot.subsystems.lasercan;

import au.grapplerobotics.LaserCan;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class laserCan extends SubsystemBase {
  LaserCan lCan;

  public laserCan() {
    lCan = new LaserCan(0);
  }
}
