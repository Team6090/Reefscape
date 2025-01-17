package frc.robot.subsystems.AdvantageKitRoller;

import static frc.robot.subsystems.AdvantageKitRoller.RollerConstants.*;
import static frc.robot.util.PhoenixUtil.*;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;

/**
 * This roller implementation is for a Talon FX driving a motor like the Falon 500 or Kraken X60.
 */
public class RollerIOTalonFX implements RollerIO {
  public final TalonFX roller = new TalonFX(59);
  private final StatusSignal<Angle> positionRot = roller.getPosition();
  private final StatusSignal<AngularVelocity> velocityRotPerSec = roller.getVelocity();
  public final StatusSignal<Voltage> appliedVolts = roller.getMotorVoltage();
  public final StatusSignal<Current> currentAmps = roller.getSupplyCurrent();

  private final VoltageOut voltageRequest = new VoltageOut(0.0);

  public RollerIOTalonFX() {
    var config = new TalonFXConfiguration();
    var limitConfig = new CurrentLimitsConfigs();
    // var talonFXConfigurator = new m_talonFXConfigs.getConfigurator();
    config.CurrentLimits.SupplyCurrentLimit = 3;
    //Set Limit on Current ^
    config.CurrentLimits.SupplyCurrentLimitEnable = true;
    //Turn limit on or off ^
    config.CurrentLimits.SupplyCurrentLowerTime = 1;
    //Time till limit drops ^
    config.CurrentLimits.SupplyCurrentLowerLimit = 1;
    //What limit drops to after time ^
    
    // config.TorqueCurrent.PeakForwardTorqueCurrent = 2;

    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    limitConfig.SupplyCurrentLimit = 0.6;
    limitConfig.SupplyCurrentLimitEnable = true;

    // talonFXConfigurator.apply(limitConfig);

    tryUntilOk(5, () -> roller.getConfigurator().apply(config, 0.25));

    BaseStatusSignal.setUpdateFrequencyForAll(
        50.0, positionRot, velocityRotPerSec, appliedVolts, currentAmps);
    roller.optimizeBusUtilization();
  }

  @Override
  public void updateInputs(RollerIOInputs inputs) {
    BaseStatusSignal.refreshAll(positionRot, velocityRotPerSec, appliedVolts, currentAmps);

    inputs.positionRad = Units.rotationsToRadians(positionRot.getValueAsDouble());
    inputs.velocityRadPerSec = Units.rotationsToRadians(velocityRotPerSec.getValueAsDouble());
    inputs.appliedVolts = appliedVolts.getValueAsDouble();
    inputs.currentAmps = currentAmps.getValueAsDouble();
  }

  @Override
  public void setVoltage(double volts) {
    roller.setControl(voltageRequest.withOutput(volts));
  }

  public void currentLimit(double c) {}
}
