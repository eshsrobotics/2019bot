package tests;

import java.util.Random;

import models.MatchDataInterface;

import org.usfirst.frc.team1759.robot.MatchData.Position;
import org.usfirst.frc.team1759.robot.MatchData.Target;

import edu.wpi.first.wpilibj.DriverStation.Alliance;

/**
 * A MatchDataInterface instance that allows the caller to specify exactly
 * where the match's targets are, or even randomize them.
 *
 * @author JasperNelson
 *
 */
public class FakeMatchData implements MatchDataInterface {

        private Alliance alliance;
        private Position startPosition;
        private Target dropoffTarget;
        private Position dropoffTargetPosition;
        private Position nearSwitchPosition;
        private Position scalePosition;
        private Position farSwitchPosition;

        /**
         * Create a MatchDataInterface instance that points to a specific target
         * and starts from a specific position.
         *
         * @param alliance Which alliance are we?
         * @param startPosition From the perspective of a driver on our alliance
         *                       side, where is our robot positioned?
         * @param nearSwitchPosition Which side of the switch closest to us is
         *                            lit in our colors?  The left side, or the
         *                            right?
         *
         *                            This is important because autonomous
         *                            generally only cares about the near
         *                            switch.
         * @param scalePosition Which side of the scale is lit in our colors?
         *                       The left side, or the right?
         * @param farSwitchPosition Which side of the switch furthest from us
         *                           is lit in our colors?  The left side, or
         *                           the right?
         * @param dropoffTarget Is our target the cente scale or is it a switch,
         *                       near or far?
         */
        public FakeMatchData(Alliance alliance, Position startPosition, Position nearSwitchPosition, Position scalePosition, Position farSwitchPosition, Target dropoffTarget) {
                this.alliance = alliance;
                this.startPosition = startPosition;
                this.nearSwitchPosition = nearSwitchPosition;
                this.scalePosition = scalePosition;
                this.farSwitchPosition = farSwitchPosition;
                this.dropoffTarget = dropoffTarget;
        }

        /**
         * Creates a fake MatchData object that assigns targets, alliances, and
         * start positions at random.
         */
        public FakeMatchData() {
                Random r = new Random();
                // Choose a random alliance.
                switch (r.nextInt(2)) {
                        case 0: this.alliance = Alliance.Blue; break;
                        case 1: this.alliance = Alliance.Red; break;
                }

                // Choose a random start position.
                switch (r.nextInt(3)) {
                        case 0: this.startPosition = Position.RIGHT; break;
                        case 1: this.startPosition = Position.CENTER; break;
                        case 2: this.startPosition = Position.LEFT; break;
                }

                // Choose a random near switch side to light up.
                switch (r.nextInt(2)) {
                        case 0: this.nearSwitchPosition = Position.RIGHT; break;
                        case 1: this.nearSwitchPosition = Position.LEFT; break;
                }

                // Choose a random scale side to light up.
                switch (r.nextInt(2)) {
                        case 0: this.scalePosition = Position.RIGHT; break;
                        case 1: this.scalePosition = Position.LEFT; break;
                }

                // Choose a random far switch side to light up.
                switch (r.nextInt(2)) {
                        case 0: this.farSwitchPosition = Position.RIGHT; break;
                        case 1: this.farSwitchPosition = Position.LEFT; break;
                }

                // Choose a random dropoff target.
                switch (r.nextInt(3)) {
                        case 0: this.dropoffTarget = Target.CLOSE_SWITCH; break;
                        case 1: this.dropoffTarget = Target.FAR_SWITCH; break;
                        case 2: this.dropoffTarget = Target.SCALE; break;
                }
        }

        @Override
        public Alliance getAlliance() {
                return this.alliance;
        }

        @Override
        public Position getNearSwitchPosition() {
                return this.nearSwitchPosition;
        }

        @Override
        public Position getFarSwitchPosition() {
                return this.farSwitchPosition;
        }

        @Override
        public Position getScalePosition() {
        	return this.scalePosition;
        }

        @Override
        public Position getOwnStartPosition() {
                return this.startPosition;
        }

        @Override
        public Target getTarget() {
                return this.dropoffTarget;
        }

}
