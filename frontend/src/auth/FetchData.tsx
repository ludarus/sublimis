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

		if (res.status === 401) {
			console.log("not logged in")
			setInfo(null);
			return;
		}
		setInfo(await res.json())
		console.log("usesr logged in " )
	} catch (error) {
		console.error(error)
	}
}
