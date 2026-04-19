// import React, { useState, useCallback, useEffect } from 'react';
// import { ReadyState } from 'react-use-websocket';
import useWebSocket from './useWebSocket.tsx'
import '../home/Home.css'

export default function Ws() {
	const { messages, sendMessage } = useWebSocket()

	// const connectionStatus = {
	// 	[ReadyState.CONNECTING]: 'Connecting',
	// 	[ReadyState.OPEN]: 'Open',
	// 	[ReadyState.CLOSING]: 'Closing',
	// 	[ReadyState.CLOSED]: 'Closed',
	// 	[ReadyState.UNINSTANTIATED]: 'Uninstantiated',
	// }[readyState];

	return (
		<div id='root-container' className=''>
			<main className=''>
				<h1>websocket demo</h1>

				<button
					onClick={sendMessage}
				// disabled={readyState != ReadyState.OPEN}
				>
					send msg
				</button>

				<p> messages: <br/>{messages} </p>

			</main>
		</div >
	)
}


