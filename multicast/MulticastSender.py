import socket
import time
import json
from datetime import datetime


def get_local_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
    finally:
        s.close()
    return ip


def startMulticastSender() -> None:
    multicastGroup: str = "224.0.0.2"
    serverPort: int = 8883

    sock: socket.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)

    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 1)

    localIp: str = get_local_ip()
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_IF, socket.inet_aton(localIp))

    print(f"Sender ativo: Enviando a cada 5 segundos de {localIp}...\n")

    try:
        while True:
            currentTime: str = datetime.now().strftime("%H:%M:%S")

            message = json.dumps({
                "time": currentTime,
                "ip": localIp
            })

            sock.sendto(message.encode("utf-8"), (multicastGroup, serverPort))

            print(f"[{currentTime}]enviado para {multicastGroup}:{serverPort}")

            time.sleep(5)

    except KeyboardInterrupt:
        print("\nSender finalizado.")

    finally:
        sock.close()


if __name__ == "__main__":
    startMulticastSender()