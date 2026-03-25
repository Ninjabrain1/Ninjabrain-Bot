# API v1 – Commands

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

Executes multiple commands in order.

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

