/*
 * Copyright 2018, OK2 Consulting Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ok2c.hc5.json;

import java.util.LinkedList;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * {@link JsonTokenEventHandler} implementation that assembles an {@link JsonNode}
 * instance based on JSON token events it receives and passes the resultant
 * object to a {@link Consumer}.
 */
public final class JsonNodeAssembler implements JsonTokenEventHandler {

    public static JsonNodeAssembler create() {
        return new JsonNodeAssembler(JsonNodeFactory.instance, null);
    }

    public static JsonNodeAssembler create(JsonNodeCreator jsonNodeCreator, Consumer<JsonNode> consumer) {
        return new JsonNodeAssembler(jsonNodeCreator, consumer);
    }

    public static JsonTokenConsumer createTokenConsumer() {
        return new JsonTokenEventHandlerAdaptor(new JsonNodeAssembler(JsonNodeFactory.instance, null));
    }

    public static JsonTokenConsumer createTokenConsumer(Consumer<JsonNode> consumer) {
        return new JsonTokenEventHandlerAdaptor(new JsonNodeAssembler(JsonNodeFactory.instance, consumer));
    }

    private final JsonNodeCreator jsonNodeCreator;
    private final Consumer<JsonNode> consumer;
    private final LinkedList<ContainerNode<?>> nodeStack;

    private JsonNode root;
    private ContainerNode<?> currentObject;
    private String currentField;
    private JsonNode result;

    public JsonNodeAssembler(JsonNodeCreator jsonNodeCreator, Consumer<JsonNode> consumer) {
        this.jsonNodeCreator = jsonNodeCreator != null ? jsonNodeCreator : JsonNodeFactory.instance;
        this.consumer = consumer;
        this.nodeStack = new LinkedList<>();
    }

    @Override
    public void objectStart() {
        if (currentObject != null) {
            ObjectNode newJsonObject;
            if (currentObject instanceof ObjectNode) {
                newJsonObject = ((ObjectNode) currentObject).putObject(currentField);
            } else if (currentObject instanceof ArrayNode) {
                newJsonObject = ((ArrayNode) currentObject).addObject();
            } else {
                throw new IllegalStateException("Unexpected node class: " + currentObject.getClass());
            }
            nodeStack.addLast(currentObject);
            currentObject = newJsonObject;
        } else {
            currentObject = jsonNodeCreator.objectNode();
            if (root == null) {
                root = currentObject;
            }
        }
    }

    @Override
    public void objectEnd() {
        currentObject = !nodeStack.isEmpty() ? nodeStack.removeLast() : null;
    }

    @Override
    public void arrayStart() {
        if (currentObject != null) {
            ArrayNode newJsonObject;
            if (currentObject instanceof ObjectNode) {
                newJsonObject = ((ObjectNode) currentObject).putArray(currentField);
            } else if (currentObject instanceof ArrayNode) {
                newJsonObject = ((ArrayNode) currentObject).addArray();
            } else {
                throw new IllegalStateException("Unexpected node class: " + currentObject.getClass());
            }
            nodeStack.addLast(currentObject);
            currentObject = newJsonObject;
        } else {
            currentObject = jsonNodeCreator.arrayNode();
            if (root == null) {
                root = currentObject;
            }
        }
    }

    @Override
    public void arrayEnd() {
        currentObject = !nodeStack.isEmpty() ? nodeStack.removeLast() : null;
    }

    @Override
    public void field(String name) {
        currentField = name;
    }

    @Override
    public void embeddedObject(Object object) {
        if (currentObject == null) {
            throw new IllegalStateException("Current node is null");
        } else if (currentObject instanceof ObjectNode) {
            ((ObjectNode) currentObject).putPOJO(currentField, object);
        } else if (currentObject instanceof ArrayNode) {
            ((ArrayNode) currentObject).addPOJO(object);
        } else {
            throw new IllegalStateException("Unexpected node class: " + currentObject.getClass());
        }
    }

    @Override
    public void value(String value) {
        if (currentObject instanceof ObjectNode) {
            ((ObjectNode) currentObject).put(currentField, value);
        } else if (currentObject instanceof ArrayNode) {
            ((ArrayNode) currentObject).add(value);
        } else {
            throw new IllegalStateException("Unexpected node class: " + currentObject.getClass());
        }
    }

    @Override
    public void value(int value) {
        if (currentObject instanceof ObjectNode) {
            ((ObjectNode) currentObject).put(currentField, value);
        } else if (currentObject instanceof ArrayNode) {
            ((ArrayNode) currentObject).add(value);
        } else {
            throw new IllegalStateException("Unexpected node class: " + currentObject.getClass());
        }
    }

    @Override
    public void value(double value) {
        if (currentObject instanceof ObjectNode) {
            ((ObjectNode) currentObject).put(currentField, value);
        } else if (currentObject instanceof ArrayNode) {
            ((ArrayNode) currentObject).add(value);
        } else {
            throw new IllegalStateException("Unexpected node class: " + currentObject.getClass());
        }
    }

    @Override
    public void value(boolean value) {
        if (currentObject instanceof ObjectNode) {
            ((ObjectNode) currentObject).put(currentField, value);
        } else if (currentObject instanceof ArrayNode) {
            ((ArrayNode) currentObject).add(value);
        } else {
            throw new IllegalStateException("Unexpected node class: " + currentObject.getClass());
        }
    }

    @Override
    public void valueNull() {
        if (currentObject instanceof ObjectNode) {
            ((ObjectNode) currentObject).putNull(currentField);
        } else if (currentObject instanceof ArrayNode) {
            ((ArrayNode) currentObject).addNull();
        } else {
            throw new IllegalStateException("Unexpected node class: " + currentObject.getClass());
        }
    }

    @Override
    public void endOfStream() {
        result = root;
        if (consumer != null) {
            consumer.accept(result);
        }
    }

    public JsonNode getResult() {
        return result;
    }

}
