import { useState, useEffect, useCallback, useRef } from 'react'

const useWebSocket = (address: string) => {
	const wsRef = useRef<WebSocket | null>(null)
	const [messages, setMessages] = useState<String[]>([]);
	// const [readyState, setReadyState] = useState(WebSocket.CLOSED)

	useEffect(() => {
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

	}, []);

	const sendMessage = useCallback(() => {
		wsRef.current?.send("hello from the frontend")
	}, [])

	return { messages, sendMessage }

}

export default useWebSocket;
