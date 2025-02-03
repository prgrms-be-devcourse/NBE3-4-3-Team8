'use client'

import { useState } from 'react'
import { useRouter, useSearchParams, usePathname } from 'next/navigation'
import { useDebouncedCallback } from 'use-debounce'

export default function Navbar() {
    const router = useRouter()
    const searchParams = useSearchParams()
    const pathname = usePathname()

    const handleSearch = useDebouncedCallback((term: string) => {
        const params = new URLSearchParams(searchParams)

        if (term) {
            params.set('query', term)
        } else {
            params.delete('query')
        }

        router.replace(`${pathname}?${params.toString()}`)
    }, 300)

    return (
        <header className="flex items-center justify-between p-4 border-b">
            <div className="text-2xl font-bold">THE BOOK</div>

            <div className="flex-1 max-w-xl mx-4">
                <input
                    type="search"
                    placeholder="Search"
                    className="w-full px-4 py-2 border rounded-lg"
                    onChange={(e) => handleSearch(e.target.value)}
                    defaultValue={searchParams.get('query')?.toString()}
                />
            </div>

            <nav className="flex gap-4">
                <a href="/log">로그인</a>
                <a href="/join">회원가입</a>
                <a href="/center">고객센터</a>
            </nav>
        </header>
    )
}
