export type WsMessage = {
	type: number
	payload: string
	time: BigInt
}

export interface ChatMessage extends WsMessage {
	name: string
	img: string
	userid: string
}

export interface ErrorMessage extends WsMessage {
	error: number
}

