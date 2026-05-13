import { useParams } from "react-router";

import useWebSocket from '../ws/useWebSocket.tsx'
import "./Live.css"

export default function Live() {
	let { campaignId } = useParams();
	const { messages, sendMessage } = useWebSocket("ws://localhost:9000/live")

	return (
		<div id="root-container-live" className="border">
			<div id="chat-container" className="border">
				<h1>{campaignId}</h1>
				<button
					onClick={sendMessage}
				// disabled={readyState != ReadyState.OPEN}
				>
					send msg
				</button>
				<p> messages: <br/>{messages} </p>
			</div>
 		<div id="game-container" className="border">
				<div id="map-container" className="border">
				</div>
				<div id="terminal-container" className="border">
				</div>
			</div>
			<div id="character-container" className="border">
			</div>
		</div>
	)
}
