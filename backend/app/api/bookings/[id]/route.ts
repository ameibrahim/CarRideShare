// app/api/bookings/[id]/route.ts
import { NextResponse } from "next/server";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

export async function GET(
    request: Request,
    { params }: { params: Promise<{ id: string }> }
) {
    const { id } = await params;

    const booking = await prisma.booking.findUnique({
        where: { id },
    });
    if (!booking) {
        return NextResponse.json(
            { error: "Booking not found" },
            { status: 404 }
        );
    }
    return NextResponse.json(booking);
}

export async function PUT(
    request: Request,
    { params }: { params: Promise<{ id: string }> }
) {
    const { id } = await params;

    const body = await request.json();
    try {
        const updatedBooking = await prisma.booking.update({
            where: { id },
            data: body,
        });
        return NextResponse.json(updatedBooking);
    } catch (error) {
        console.log("error: ", error);
        return NextResponse.json(
            { error: "Failed to update booking" },
            { status: 500 }
        );
    }
}

export async function DELETE(
    request: Request,
    { params }: { params: Promise<{ id: string }> }
) {
    const { id } = await params;
    try {
        await prisma.booking.delete({ where: { id } });
        return NextResponse.json({ message: "Booking deleted" });
    } catch (error) {
        console.log("error: ", error);
        return NextResponse.json(
            { error: "Failed to delete booking" },
            { status: 500 }
        );
    }
}
