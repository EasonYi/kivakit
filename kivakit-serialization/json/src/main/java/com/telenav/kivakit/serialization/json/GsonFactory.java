package com.telenav.kivakit.serialization.json;

import com.google.gson.Gson;
import com.telenav.kivakit.kernel.messaging.Repeater;

public interface GsonFactory extends Repeater
{
    Gson gson();
}
