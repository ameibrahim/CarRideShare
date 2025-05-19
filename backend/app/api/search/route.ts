import { NextResponse } from "next/server";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

export async function POST(request: Request) {
    try {
        const { search } = await request.json();
        if (!search || typeof search !== "string") {
            return NextResponse.json(
                { error: "Invalid search parameter" },
                { status: 400 }
            );
        }
        
        const results = await prisma.ride.findMany({
            where: {
                OR: [
                    { startLocation: { contains: search } },
                    { endLocation: { contains: search} },
                    { name: { contains: search } }
                ]
            },
            orderBy: { createdAt: "desc" },
            take: 10,
        });
        
        return NextResponse.json({ results });
    } catch (error) {
        console.error("Error during search:", error);
        return NextResponse.json(
            { error: "Search failed" },
            { status: 500 }
        );
    }
}

