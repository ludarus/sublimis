import type { ChatMessage } from "../types/Message"

type ChatElementProps = {
	chatToggled: boolean
	playerCountToggled: boolean
	messages: ChatMessage[]
	playerlist: string[] | null
	sendMessage: (msg: FormData) => void
}

export default function ChatContent({ chatToggled, playerCountToggled, messages, playerlist, sendMessage }: ChatElementProps) {

	if (chatToggled) {
		if (playerCountToggled) {
			if (playerlist) {
				console.log(playerlist)
				return (
					<ul id="player-list" className="border border-yellow-500" >
						{
							playerlist.map((player) => (
								<li className="player" key={player}>
									here is da player {player}
								</li>
							))
						}
					</ul>
				)
			} else {
				return (
					// TODO: should request playerlist from the server
					<p>loading...</p>
				)
			}
		} else {
			return (
				<div id="chat-content">
					<ul id="messages" className="border border-green-500" >
						{
							messages.map((msg) => (
								<li className="message" key={Number(msg.time)}>
									<div className="message-image">
										<img className="rounded-full " src={msg.img} />
									</div>
									<div className="message-text">

										<div className="message-headings">
											<h2><b>{msg.name}</b></h2>

											<h3>{new Date(Number(msg.time)).toLocaleTimeString()}</h3>
										</div>
										<p className="message-payload">
											{msg.payload}
										</p>
									</div>
								</li>
							))
						}
					</ul>
					<form action={sendMessage} id="chat-form" >

						<input type="text" className="border-2" id="chat-box" name="chat-msg" />
					</form>
				</div>
			)
		}
	} else {
		return (
			<div />
		)
	}
} 
