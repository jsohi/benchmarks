/*
 * Copyright 2015-2024 Real Logic Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.benchmarks.aeron.remote;

import io.aeron.Aeron;
import io.aeron.RethrowingErrorHandler;
import io.aeron.driver.MediaDriver;
import org.HdrHistogram.ValueRecorder;
import org.agrona.concurrent.NanoClock;
import org.junit.jupiter.api.AfterEach;

import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.aeron.Aeron.connect;
import static java.lang.System.clearProperty;
import static org.mockito.Mockito.mock;
import static uk.co.real_logic.benchmarks.aeron.remote.AeronUtil.*;

class EchoTest extends AbstractTest<MediaDriver, Aeron, EchoMessageTransceiver, EchoNode>
{
    protected EchoNode createNode(final AtomicBoolean running, final MediaDriver mediaDriver, final Aeron aeron)
    {
        return new EchoNode(running, mediaDriver, aeron, false, mock(PrintStream.class));
    }

    protected MediaDriver createDriver()
    {
        return launchEmbeddedMediaDriverIfConfigured();
    }

    protected Aeron connectToDriver()
    {
        return connect(new Aeron.Context().errorHandler(new RethrowingErrorHandler()));
    }

    protected Class<EchoMessageTransceiver> messageTransceiverClass()
    {
        return EchoMessageTransceiver.class;
    }

    protected EchoMessageTransceiver createMessageTransceiver(
        final NanoClock nanoClock,
        final ValueRecorder valueRecorder,
        final MediaDriver mediaDriver,
        final Aeron aeron)
    {
        return new EchoMessageTransceiver(nanoClock, valueRecorder, mediaDriver, aeron, false);
    }

    @AfterEach
    void after()
    {
        super.after();
        clearProperty(DESTINATION_CHANNELS_PROP_NAME);
        clearProperty(DESTINATION_STREAMS_PROP_NAME);
        clearProperty(SOURCE_CHANNELS_PROP_NAME);
        clearProperty(SOURCE_STREAMS_PROP_NAME);
    }
}
