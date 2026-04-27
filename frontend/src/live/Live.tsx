import { useParams } from "react-router";

import "./Live.css"

export default function Live() {
	let { campaignId } = useParams();

	return (
		<div id="root-container-live" className="border">
			<div id="chat-container" className="border">
				<h1>{campaignId}</h1>
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
