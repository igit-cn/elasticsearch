/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.core.ml.action;

import org.elasticsearch.Version;
import org.elasticsearch.common.ValidationException;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.xpack.core.ml.AbstractBWCWireSerializationTestCase;
import org.elasticsearch.xpack.core.ml.action.PutTrainedModelDefinitionPartAction.Request;

import static org.hamcrest.Matchers.containsString;

public class PutTrainedModelDefinitionPartActionRequestTests extends AbstractBWCWireSerializationTestCase<Request> {

    @Override
    protected Request createTestInstance() {
        return new Request(
            randomAlphaOfLength(20),
            new BytesArray(randomAlphaOfLength(20)),
            randomIntBetween(0, 10),
            randomLongBetween(1, Long.MAX_VALUE),
            randomIntBetween(10, 100)
        );
    }

    public void testValidate() {
        Request badRequest = new Request(randomAlphaOfLength(10), new BytesArray(randomAlphaOfLength(10)), -1, -1, -1);

        ValidationException exception = badRequest.validate();
        assertThat(exception.getMessage(), containsString("[part] must be greater or equal to 0"));
        assertThat(exception.getMessage(), containsString("[total_parts] must be greater than 0"));
        assertThat(exception.getMessage(), containsString("[total_definition_length] must be greater than 0"));

        badRequest = new Request(randomAlphaOfLength(10), new BytesArray(randomAlphaOfLength(10)), 5, 10, 5);

        exception = badRequest.validate();
        assertThat(exception.getMessage(), containsString("[part] must be less than total_parts"));
    }

    @Override
    protected Writeable.Reader<Request> instanceReader() {
        return Request::new;
    }

    @Override
    protected Request mutateInstanceForVersion(Request instance, Version version) {
        return instance;
    }
}
