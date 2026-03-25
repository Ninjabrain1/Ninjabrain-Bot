# API v1 – Commands

All interactions with Ninjabrain Bot through the input API happens with _commands_. They allow external tools and scripts to control the application programmatically.

Commands are executed by sending `POST` requests to one of the endpoints below.

## Endpoints

### `POST /api/v1/send-command`

Executes a single command.

**Request body:**

```json
{
  "command": "<command_name>",
  "parameters": { ... }
}
```

The `parameters` property should be omitted for commands that take no parameters.

### `POST /api/v1/send-commands`

Executes multiple commands in sequence within a single request. Commands are validated upfront before any are executed. If any command in the batch is malformed or has invalid parameters, the entire request is rejected and no commands are executed.

Each command is executed in order, and its effects are applied to the application state before the next command runs. This means later commands in the batch can depend on state changes made by earlier ones.
All commands are executed in one transaction, meaning that no intermediate state is visible to other API clients or the UI until the entire batch has completed.
~~~~
An undo or redo action, executed through the API or the GUI, will undo or redo all commands in the most recently completed batch as a single unit. This means that if you send a batch of 5 commands, a single undo will revert all 5 commands together, rather than requiring 5 separate undo actions.

**Request body:**

```json
{
  "commands": [
    { "command": "<command_name>", "parameters": { ... } },
    { "command": "<command_name>" }
  ]
}
```

## Commands

| Command | Description |
| --- | --- |
{{commands_table}}
---

{{command_details}}

