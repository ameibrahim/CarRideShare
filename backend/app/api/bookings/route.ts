// app/api/bookings/route.ts
import { NextResponse } from "next/server";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

export async function GET() {
    const bookings = await prisma.booking.findMany();
    return NextResponse.json(bookings);
}

export async function POST(request: Request) {
    const body = await request.json();
    const { rideId, userId, costPerPassenger } = body;
    try {
        const booking = await prisma.booking.create({
            data: { rideId, userId, costPerPassenger },
        });
        return NextResponse.json(booking, { status: 201 });
    } catch (error) {
        console.log("error: ", error);

        return NextResponse.json(
            { error: "Failed to create booking" },
            { status: 500 }
        );
    }
}
