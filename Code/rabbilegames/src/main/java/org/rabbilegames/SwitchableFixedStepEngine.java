package org.rabbilegames;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.util.FPSCounter;
import org.andengine.util.time.TimeConstants;

public class SwitchableFixedStepEngine extends Engine
{

    // ====================================================
    // VARIABLES
    // ====================================================
    private final long mStepLength;
    private long mSecondsElapsedAccumulator;
    private boolean mFixedStepEnabled = false;
    public final int mStepsPerSecond;
    public final FPSCounter FpsCounter = new FPSCounter();

    // ====================================================
    // CONSTRUCTOR
    // ====================================================
    public SwitchableFixedStepEngine(final EngineOptions pEngineOptions, final int pStepsPerSecond, final boolean pFixedStepEnabled)
    {
        super(pEngineOptions);
        this.mStepLength = TimeConstants.NANOSECONDS_PER_SECOND / pStepsPerSecond;
        mFixedStepEnabled = pFixedStepEnabled;
        mStepsPerSecond = pStepsPerSecond;
    }

    // ====================================================
    // METHODS
    // ====================================================
    public void EnableFixedStep()
    {
        mFixedStepEnabled = true;
    }

    public void DisableFixedStep()
    {
        mFixedStepEnabled = false;
    }

    @Override
    public void onUpdate(final long pNanosecondsElapsed) throws InterruptedException
    {
        FpsCounter.onUpdate(pNanosecondsElapsed / 1000000000f);
        if (mFixedStepEnabled)
        {
            this.mSecondsElapsedAccumulator += pNanosecondsElapsed;

            final long stepLength = this.mStepLength;
            while (this.mSecondsElapsedAccumulator >= stepLength)
            {
                super.onUpdate(stepLength);
                this.mSecondsElapsedAccumulator -= stepLength;
            }
        }
        else
        {
            this.mSecondsElapsedAccumulator = 0;
            super.onUpdate(pNanosecondsElapsed);
        }
    }
}
