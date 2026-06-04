import { useState, useEffect, useCallback, useRef } from 'react'
import type { ChatMessage, WsMessage, ErrorMessage } from '../types/ChatMessage';

export default function useWebSocket(address: string, enabled: boolean) {
	const wsRef = useRef<WebSocket | null>(null)
	const [messages, setMessages] = useState<ChatMessage[]>([]);
	const [error, setError] = useState<number | null>(null)
	const [ready, setReady] = useState<boolean>(false)
	// const [readyState, setReadyState] = useState<WebSocket["readyState"]>(WebSocket.CONNECTING)

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

		newSocket.onerror = (err) => {
			console.log("cannot connect ")
			console.log(err)
		}

		newSocket.onmessage = (event) => {
			// console.log("recieved message " + event.data as string)
			try {
				const data: WsMessage = JSON.parse(event.data as string)
				if (data.type === 1) {
					// message = chatMessage
					setMessages((prevMessages) => [...prevMessages, data as ChatMessage])
				} else if (data.type === 0) {
					//message = errormessage
					setError((data as ErrorMessage).error)
					setReady(true)
					newSocket.close()
				} else if (data.type === 3) {
					//ready event
					setReady(true)
				}
			} catch (error) {
				console.error(error)
			}
		}

		newSocket.onclose = (event) => {
			console.log("closed", {
				code: event.code,
				reason: event.reason,
				wasClean: event.wasClean
			})

			switch (event.code) {
				case 4001:
					console.error("Unauthorized")
					break

				case 4002:
					console.error("Subscription expired")
					break

				case 4003:
					console.error("Rate limited")
					break

				case 1006:
					console.error("Connection idle time out")
					break

				default:
					console.error("Unknown websocket close")
			}
		}

		return () => {
			newSocket.close()
		}

	}, [enabled]);

	const sendMessage = useCallback((msg: FormData) => {
		const formStuff = msg.get("chat-msg")
		wsRef.current?.send(String(formStuff ?? ""))
	}, [])

	return { messages, sendMessage, ready, error }
}
