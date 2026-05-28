import Link from "next/link";

export default function UserPost() {
    return (
        <div className="max-w-2xl mx-auto">
            <div className="flex flex-col gap-2 text-center mb-8">
                <Link href="/userPost/create">
                    <button className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600">New announcements</button>
                </Link>

                {/*Your posts */}
                <p className="text-gray-500 text-sm md:text-base max-w-2xl mx-auto">
                    Review, edit, and track all the content you've shared with the community.
                </p>
            </div>
        </div>
    )
}