import asyncio
from websockets.asyncio.server import serve, broadcast

sessions = set()
messages = "<br>"

async def socketStart():
    async with serve(connect, "0.0.0.0", 5001):
        await asyncio.get_running_loop().create_future()

async def connect(socket):
    sessions.add(socket)
    await socket.send(messages)
    try:
        async for message in socket:
            messages += message + "<br>"
            broadcast(sessions, message + "<br>")
    finally:
        sessions.remove(socket)

asyncio.run(socketStart())