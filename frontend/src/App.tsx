import { useState, useEffect } from 'react'
import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google'

// import reactLogo from './assets/react.svg'
// import viteLogo from './assets/vite.svg'
// import heroImg from './assets/hero.png'
import './App.css'



function App() {

	return (
		<GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}>
			<div>
				<h1>Welcome, </h1>
				<p>
					This is is the sample text for Project sublimis, a DND hosting platform.
				</p>
				<br />
				<PostButton />
				<br />
				<div className="google-login-wrapper">
					<GoogleLogin
						onSuccess={credentialReponse => {
							//send token to backend to process shit
							console.log(credentialReponse);
							console.log("fiuhh")
							//sending the token to the backend to get verified and logged
							fetch(
								'http://localhost:9000/googleAuth',
								{
									method: 'POST',
									headers: { 'Content-Type': 'application/json' },
									body: JSON.stringify(credentialReponse)
									// body: JSON.stringify({"credential":"sdlkfjalkjf"})
								}
							).then(response => console.log(response))

						}}
						onError={() => { console.log('failure'); }}
					/>
				</div>

			</div>
		</GoogleOAuthProvider>

	)
}


//random shit::
type LoginButtonProps = {
	logged: boolean;
}
function LoginButton({ logged }: LoginButtonProps) {
	return (
		<div>
			<button onClick={function() { alert('hi') }}
				className="
			rounded-2xl 
			outline-1 
			outline-black
			">
				{logged ? <p>Welcome back!</p> : <p>Please sign in.</p>}
			</button>
		</div>
	)
}

function PostButton() {
	async function postData(msg: String) {
		await fetch(
			'http://localhost:9000/dump',
			{
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(msg),
				credentials: 'include'
			}
		).then(response => console.log(response))
		// .then(data => console.log(data))
		// .catch(error => console.log(error))
	}
	return (
		<div>
			<button onClick={() => postData("this is a message")}
				className="outline-2 bg-white">
				post something
			</button>
		</div>
	)
}

function SendButton() {
	const [data, setData] = useState(null)
	async function fetchData() {
		const res = await fetch("http://localhost:9000/assets/json/test.json");
		var json = await res.json()
		if (data != null) {
			json = null
		}
		setData(json);
	}
	return (
		<div>
			<button className="
			outline-2 
			outline-black 
			bg-white 
			" onClick={fetchData}>
				{data ? "unload data" : "fetch data"}
			</button>
			<p>
				{data ? JSON.stringify(data) : "data not loaded..."}
			</p>
		</div>

	)
}
export default App
