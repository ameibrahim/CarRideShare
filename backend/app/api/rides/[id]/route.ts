// app/api/rides/[id]/route.ts
import { NextResponse } from "next/server";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

export async function GET(
    request: Request,
    { params }: { params: { id: string } }
) {
    const ride = await prisma.ride.findUnique({ where: { id: params.id } });
    if (!ride) {
        return NextResponse.json({ error: "Ride not found" }, { status: 404 });
    }
    return NextResponse.json(ride);
}

export async function PUT(
    request: Request,
    { params }: { params: { id: string } }
) {
    const body = await request.json();
    try {
        const updatedRide = await prisma.ride.update({
            where: { id: params.id },
            data: body,
        });
        return NextResponse.json(updatedRide);
    } catch (error) {
        console.log("error: ", error);

        return NextResponse.json(
            { error: "Failed to update ride" },
            { status: 500 }
        );
    }
}

export async function DELETE(
    request: Request,
    { params }: { params: { id: string } }
) {
    try {
        await prisma.ride.delete({ where: { id: params.id } });
        return NextResponse.json({ message: "Ride deleted" });
    } catch (error) {
        console.log("error: ", error);

        return NextResponse.json(
            { error: "Failed to delete ride" },
            { status: 500 }
        );
    }
}
