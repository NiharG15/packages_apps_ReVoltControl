package com.revolt.control.service;

import android.app.IntentService;
import android.content.Intent;
import com.revolt.control.util.CMDProcessor;

/**
 * Helper class to queue su commands that can be run from broadcast receivers,
 * specifically like ones defined in AndroidManifest.xml, as they are run in the
 * main thread, and starting async operations does not guarantee execution. This
 * class should not be accessible from outside ROMControl. Commands will be queued.
 */
public class ExternalCommandService extends IntentService {

    public ExternalCommandService() {
        super(ExternalCommandService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.hasExtra("cmd")) {
            CMDProcessor.startSuCommand(
                    intent.getStringExtra("cmd"));
        }
    }
}
