package frc.robot.subsystems.Testing;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.AdvantageKitRoller.RollerIO;
import java.util.function.DoubleSupplier;

public class advantagekitMotor extends SubsystemBase {

  public RollerIO io;
  public TalonFX test;

  public advantagekitMotor(RollerIO io) {
    test = new TalonFX(59);
    this.io = io;
  }

  public void motorTester(double power) {
    test.setVoltage(power);
  }

  public void motorLimit(DoubleSupplier forward, DoubleSupplier reverse) {
    // if (test.getStatorCurrent().getValueAsDouble() > 0.2) {
    //   test.setVoltage(0);
    // } else {
    test.setVoltage(forward.getAsDouble() - reverse.getAsDouble() * 12);
    // }
    // return runEnd(
    //     () -> io.setVoltage((forward.getAsDouble() - reverse.getAsDouble()) * 12),
    //     () -> io.setVoltage(0));
  }
}
