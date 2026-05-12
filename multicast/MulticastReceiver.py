import socket
import struct
import threading
import time
from datetime import datetime
from typing import Dict, List
import json


suspectsList: List[Dict[str, float]] = []
statsData: Dict[str, int] = {}
statsLock: threading.Lock = threading.Lock()

multicastGroup: str = "224.0.0.2"
serverPort: int = 8883

sock: socket.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
sock.bind(("", serverPort))


def get_local_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect(("8.8.8.8", 80))
        return s.getsockname()[0]
    finally:
        s.close()



def updateSuspect(ip: str, count: int, expected: int):
    lost = max(0, expected - count)
    failPercent = (lost / expected) * 100

    for item in suspectsList:
        if item["ip"] == ip:
            item["suspeito"] = failPercent
            return

    suspectsList.append({"ip": ip, "suspeito": failPercent})



def generateReport() -> None:
    windowSeconds: int = 60
    expectedPackets: int = 12  

    while True:
        time.sleep(windowSeconds)
        now: str = datetime.now().strftime("%H:%M:%S")

        with statsLock:
            if not statsData:
                print("Nenhuma atividade detectada.")
                continue

            for ip, count in statsData.items():
                updateSuspect(ip, count, expectedPackets)

            localIp = get_local_ip()

            message = json.dumps({
                "time": now,
                "window": windowSeconds,
                "ip": localIp,
                "data": suspectsList
            })

            sock.sendto(message.encode("utf-8"), (multicastGroup, serverPort))

            print(f"\n[{now}] Relatório enviado:")
            print(message)

            for ip in statsData:
                statsData[ip] = 0



def startMulticastReceiver() -> None:
    mreq: bytes = struct.pack("4sl", socket.inet_aton(multicastGroup), socket.INADDR_ANY)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

    threading.Thread(target=generateReport, daemon=True).start()

    print(f"Escutando em {multicastGroup}:{serverPort}...\n")

    try:
        while True:
            data, addr = sock.recvfrom(2048)
            rawMsg: str = data.decode("utf-8")
            arrival: str = datetime.now().strftime("%H:%M:%S.%f")[:-3]

            try:
                message = json.loads(rawMsg)
            except json.JSONDecodeError:
                print(f"[ERRO] Mensagem inválida: {rawMsg}")
                continue

 
            if len(message) == 2:
                senderIp = message["ip"]

                with statsLock:
                    statsData[senderIp] = statsData.get(senderIp, 0) + 1

                print(f"[LOG {arrival}]  recebido de {senderIp}")

            elif len(message) == 4:
                senderIp = message["ip"]
                receivedList = message["data"]

                myIp = get_local_ip()

                receivedMySuspect = next(
                    (item["suspeito"] for item in receivedList if item["ip"] == myIp),
                    None
                )

                localSenderSuspect = next(
                    (item["suspeito"] for item in suspectsList if item["ip"] == senderIp),
                    None
                )

                print(f"\n Comparação:")
                print(f"Meu suspeito: {localSenderSuspect}")
                print(f"Suspeito recebido de {senderIp}: {receivedMySuspect}\n")

    except KeyboardInterrupt:
        print("\nReceiver finalizado.")
    finally:
        sock.close()


if __name__ == "__main__":
    startMulticastReceiver()