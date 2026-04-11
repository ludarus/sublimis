import { useState, useEffect } from 'react'
import { GoogleOAuthProvider, GoogleLogin, type CredentialResponse } from '@react-oauth/google'
import './App.css'

function App() {
	const [userInfo, setInfo] = useState(null)

	return (
		<GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}>
			<div>
				<h1>Welcome, </h1>
				<p>
					This is is the sample text for Project sublimis, a DND hosting platform.
				</p>
				<br />
				<br />
				<div className="google-login-wrapper">
					<GoogleLogin
						onSuccess={credentialReponse => {
							//send token to backend to process shit
							console.log(credentialReponse);
							//sending the token to the backend to get verified and logged
							googleLogin(credentialReponse)
						}
						}
					/>
				</div>
				<br />
				<hr />
				<br />
				<p> {userInfo ? JSON.stringify(userInfo) : "You are not logged in yet"}</p>
			</div>
		</GoogleOAuthProvider>

	)


//inline functions that arent actually inline 
async function googleLogin(credentialReponse: CredentialResponse) {
		const res = await fetch(
			'http://localhost:9000/googleAuth', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(credentialReponse),
			credentials: 'include'
		}
		)

		var json = await res.json()
		setInfo(json)

	}
}

export default App
