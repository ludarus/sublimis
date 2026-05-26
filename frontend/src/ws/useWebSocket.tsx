import { useState, useEffect, useCallback, useRef } from 'react'

export default function useWebSocket(address: string, enabled: boolean) {
	const wsRef = useRef<WebSocket | null>(null)
	const [messages, setMessages] = useState<String[]>([]);

	// automatically opens websocket connection
	useEffect(() => {
		if (!enabled) {
			return;
		}


		const newSocket = new WebSocket(address)
		wsRef.current = newSocket;

		newSocket.onopen = () => {
			console.log("opened new socket")
		}

		newSocket.onmessage = (event) => {
			// console.log("recieved message " + event.data as string)
			setMessages((prevMessages) => [...prevMessages, event.data as string])
		}

		newSocket.onclose = () => {
			console.log('socket closed')
		}

		return () => {
			newSocket.close()
		}

	}, [enabled]);

	const sendMessage = useCallback((msg: FormData) => {
		const formStuff = msg.get("chat-msg")
		wsRef.current?.send(String(formStuff ?? ""))
	}, [])

	return { messages, sendMessage }
}
