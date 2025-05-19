// app/api/rides/[id]/route.ts
import { NextResponse } from "next/server";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

// eslint-disable-next-line @typescript-eslint/no-unused-vars
export async function GET(request: Request) {
    try {
        const rides = await prisma.ride.findMany({
            orderBy: { createdAt: "desc" },
            take: 3,
        });
        return NextResponse.json({ rides });
    } catch (error) {
        console.error("Error fetching rides:", error);
        return NextResponse.json(
            { error: "Failed to fetch rides" },
            { status: 500 }
        );
    }
}
