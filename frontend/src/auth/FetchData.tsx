//fetches user data on refresh from backend
import type { Dispatch, SetStateAction } from "react"
import type { SublimisUser } from '../types/SublimisUser.tsx'

export default async function fetchData(setInfo: Dispatch<SetStateAction<SublimisUser | null>>) {
	try {
		const res = await fetch(
			'http://localhost:9000/getUserInfo',
			{
				method: 'GET',
				credentials: 'include'
			}
		)

		//checking result status
		if (!res.ok) {
			if (res.status === 401) {
				console.log("Not logged in");
				setInfo(null);
				return;
			}

			console.log(`Request failed with status ${res.status}`);
			setInfo(null);
			return;
		}

		setInfo(await res.json())
		console.log("usesr logged in ")
	} catch (error) {
		console.error(error)
	}
}
