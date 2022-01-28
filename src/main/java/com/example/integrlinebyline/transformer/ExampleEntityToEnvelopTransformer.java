package com.example.integrlinebyline.transformer;

import com.example.integrlinebyline.dto.ExampleEntity;
import com.example.integrlinebyline.envelope.ExampleBodyEnvelope;
import com.example.integrlinebyline.envelope.ExampleEnvelope;
import com.example.integrlinebyline.envelope.ExampleHeaderEnvelope;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class ExampleEntityToEnvelopTransformer extends AbstractPayloadTransformer<ExampleEntity, ExampleEnvelope> {
    @Override
    protected ExampleEnvelope transformPayload(ExampleEntity payload) {
        ExampleEnvelope envelope = new ExampleEnvelope();
        envelope.header = new ExampleHeaderEnvelope();
        envelope.body = new ExampleBodyEnvelope();

        envelope.body.id = payload.body.id;
        envelope.body.docNumber = payload.body.docNumber;
        envelope.body.fileDest = payload.body.fileDest;

        return envelope;
    }
}
