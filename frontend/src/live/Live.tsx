import { useParams } from "react-router";

import useWebSocket from '../ws/useWebSocket.tsx'
import "./Live.css"

export default function Live() {
	let { campaignId } = useParams();
	//sending campaignId in url
	const { messages, sendMessage } = useWebSocket(`ws://localhost:9000/live/${campaignId}`)

	return (
		<div id="root-container-live" className="border">
			<div id="chat-container" className="border">
				<h1>{campaignId}</h1>

				<form action={sendMessage}>
					<input type="text" className="border-2" id="chat-box" name="chat-msg" />
				</form>
				<p> messages: <br />{messages} </p>
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
