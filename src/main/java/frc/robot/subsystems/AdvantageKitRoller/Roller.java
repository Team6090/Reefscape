package frc.robot.subsystems.AdvantageKitRoller;

import edu.wpi.first.wpilibj.simulation.PDPSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.conduit.schema.PDPData;
import org.littletonrobotics.junction.Logger;

public class Roller extends SubsystemBase {
  private final RollerIO io;
  private final RollerIOInputsAutoLogged inputs = new RollerIOInputsAutoLogged();
  RollerIOTalonFX rollerIOTalonFX;
  PDPData pdpData;
  PDPSim pdpSim;

  public Roller(RollerIO io) {
    this.io = io;
    pdpData = new PDPData();
    pdpSim = new PDPSim(0);
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Roller", inputs);
  }

  public Command runPercent(double percent) {
    return runEnd(() -> io.setVoltage(percent * 12.0), () -> io.setVoltage(0.0));
  }

  public Command runTeleop(DoubleSupplier forward, DoubleSupplier reverse) {
    return runEnd(
        () -> io.setVoltage((forward.getAsDouble() - reverse.getAsDouble()) * 2.0),
        () -> io.setVoltage(0.0));
  }

  /*TODO*/
  /*GET THE MOTOR TO STOP OVER .2 AMPS*/

  public Command stopTeleop(DoubleSupplier forward, DoubleSupplier reverse) {
    if (rollerIOTalonFX.currentAmps.getValueAsDouble() > 0.2) {
      return runOnce(() -> io.setVoltage(0.0));
    } else {
      return runEnd(
          () -> io.setVoltage((forward.getAsDouble() - reverse.getAsDouble()) * 12.0),
          () -> io.setVoltage(0.0));
    }
  }

  public Command pdpLimit(DoubleSupplier forward, DoubleSupplier reverse) {
    if (pdpSim.getCurrent(11) > 0.2) {
      pdpSim.setCurrent(11, 0);
    }
    return runEnd(
        () -> io.setVoltage((forward.getAsDouble() - reverse.getAsDouble()) * 5),
        () -> io.setVoltage(0.0));
  }

  // public Command rpmStopLimit(DoubleSupplier forward, DoubleSupplier reverse){
  //   return runEnd(
  //     ,
  //   )
  // }

  /* Get Motor To Work **
   * Get MotorCurrent  **
   * Use MotorCurrent to do something
   * Use MotorCurrent to stop Motor
   */
}
